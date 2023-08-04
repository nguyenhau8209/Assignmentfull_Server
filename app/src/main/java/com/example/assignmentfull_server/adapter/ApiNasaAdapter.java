package com.example.assignmentfull_server.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmentfull_server.R;
import com.example.assignmentfull_server.api.ApiServer;
import com.example.assignmentfull_server.model.DataNasa;
import com.bumptech.glide.Glide;
import com.example.assignmentfull_server.model.DataServer;

import java.util.Base64;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiNasaAdapter extends RecyclerView.Adapter<ApiNasaAdapter.DataNasaViewHolder> {

    private Context context;
    private List<DataNasa> list;

    public ApiNasaAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DataNasa>list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DataNasaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_adapter_apinasa, parent, false);
        return new DataNasaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataNasaViewHolder holder, int position) {
        DataNasa obj = list.get(position);
        if (obj == null) {
            return;
        }
        String url = decordByBase64(obj.getUrl());
        Glide.with(context)
                .load(url)
                .error(R.drawable.baseline_error_24)
                .into(holder.imgImageApiNasa);
        holder.bindData(obj);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DataNasaViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgImageApiNasa;
        private TextView tvTitle, tvContent;

        public DataNasaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgImageApiNasa = itemView.findViewById(R.id.imgImageApiNasa);
            tvTitle = itemView.findViewById(R.id.tvTitleApiNasa);
            tvContent = itemView.findViewById(R.id.tvContentApiNasa);

            ImageButton btnDelele = itemView.findViewById(R.id.btnDelete);
            btnDelele.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        DataNasa obj = list.get(position);
                        showDeleteComfirmDialog(position, obj.get_id());
                    }
                }
            });
            ImageButton btnEdit = itemView.findViewById(R.id.btnEdit);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        DataNasa obj = list.get(position);
                        showUpdateDialog(obj, position);
                    }
                }
            });
        }

        public void bindData(DataNasa dataNasa) {
            tvTitle.setText(dataNasa.getTitle());
            tvContent.setEllipsize(TextUtils.TruncateAt.END);
            tvContent.setText(dataNasa.getExplanation());
        }
    }

    private void showUpdateDialog(DataNasa obj, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_image, null);
        builder.setView(dialogView);

        //anh xa cac phan tu
        EditText etTitleEdit = dialogView.findViewById(R.id.etTitleEdit);
        EditText etContentEdit = dialogView.findViewById(R.id.etContentEdit);
        ImageView imgImageEdit = dialogView.findViewById(R.id.imgEditImage);
        Button btnSaveEdit = dialogView.findViewById(R.id.btnSaveEdit);
        Button btnCancelEdit = dialogView.findViewById(R.id.btnCancelEdit);

        //set data len view
        etTitleEdit.setText(obj.getTitle());
        etContentEdit.setText(obj.getExplanation());
        String url = decordByBase64(obj.getUrl());
        Glide.with(context)
                .load(url)
                .error(R.drawable.baseline_error_24)
                .into(imgImageEdit);

        AlertDialog dialog = builder.create();
        dialog.show();

        //onClickSave
        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = etTitleEdit.getText().toString();
                String newContent = etContentEdit.getText().toString();
                updateData(obj.get_id(), newTitle, newContent, position);

                dialog.dismiss();
            }
        });
        //onclick cancel
        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void updateData(String id, String newTitle, String newContent, int position) {
        DataNasa updateData = new DataNasa();
        updateData.setTitle(newTitle);
        updateData.setExplanation(newContent);

        //Call api to update
        ApiServer.apiServer.updateData(id, updateData).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    DataNasa obj = list.get(position);
                    obj.setTitle(newTitle);
                    obj.setExplanation(newContent);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Update to api false", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Call api to update false", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String decordByBase64(String url) {
        String newUrl = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Base64.Decoder base64Decoder = Base64.getUrlDecoder();
                byte[] bytes = base64Decoder.decode(url);
                newUrl = new String(bytes);
            } catch (IllegalArgumentException exception) {
                Log.e("Error", "decordByBase64: " + exception.getMessage());
            }
        }
        return newUrl;
    }

    private void deleteItem(int position, String id) {
        ApiServer.apiServer.deleteData(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    list.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDeleteComfirmDialog(int posision, String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete")
                .setMessage("You're sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(posision, id);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
