package com.example.lab3_and102_fa24.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3_and102_fa24.MainActivity;
import com.example.lab3_and102_fa24.R;
import com.example.lab3_and102_fa24.dao.TaskInfoDAO;
import com.example.lab3_and102_fa24.models.TaskInfo;

import java.util.ArrayList;

public class TaskInfoAdapter extends RecyclerView.Adapter<TaskInfoAdapter.ViewHolderInfo>{
    Context context;
    ArrayList<TaskInfo> list;
    TaskInfoDAO taskInfoDAO;

    public TaskInfoAdapter(Context context, ArrayList<TaskInfo> list) {
        this.context = context;
        this.list = list;
        taskInfoDAO = new TaskInfoDAO(context);
    }

    @NonNull
    @Override
    public ViewHolderInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task_info, parent, false);
        return new ViewHolderInfo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderInfo holder, @SuppressLint("RecyclerView") int position) {
        holder.tvContent.setText(list.get(position).getContent());
        holder.tvDate.setText(list.get(position).getDate());

        if (list.get(position).getStatus() == 1){
            holder.chkTask.setChecked(true);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.chkTask.setChecked(false);
            holder.tvContent.setPaintFlags(holder.tvContent.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.chkTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int id = list.get(holder.getAdapterPosition()).getId();
                boolean checkRS = taskInfoDAO.updateTypeInfo(id, holder.chkTask.isChecked());
                if (checkRS){
                    Toast.makeText(context, "Update succesful", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Update faile", Toast.LENGTH_SHORT).show();
                }
                list.clear();
                list = taskInfoDAO.getListInfo();
//                notifyDataSetChanged();
            }
        });

        holder.getImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = list.get(holder.getAdapterPosition()).getId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete information");
                builder.setIcon(R.drawable.icon_delete);
                builder.setMessage("Are you sure to delete?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean check = taskInfoDAO.removeInfo(id);
                        if(check){
                            Toast.makeText(context.getApplicationContext(), "Delete succesful", Toast.LENGTH_SHORT).show();
                            list.clear();
                            list = taskInfoDAO.getListInfo();
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                        else {
                            Toast.makeText(context.getApplicationContext(), "Delete faile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.imgUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
                View view1 = inflater.inflate(R.layout.custom_dialog_update,null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view1);
                EditText edtID, edtTitle, edtContent, edtDate, edtType;
                edtID = view1.findViewById(R.id.edtID);
                edtTitle = view1.findViewById(R.id.edtTitle);
                edtContent = view1.findViewById(R.id.edtContent);
                edtDate = view1.findViewById(R.id.edtDate);
                edtType = view1.findViewById(R.id.edtType);

                //load data len dialog
                edtID.setText(String.valueOf(list.get(position).getId()));
                edtTitle.setText(list.get(position).getTitle());
                edtContent.setText(list.get(position).getContent());
                edtDate.setText(list.get(position).getDate());
                edtType.setText(list.get(position).getType());

                edtType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] arrType = {"De", "Trung binh", "Kho"};
                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                        builder.setTitle("Please choose level");
                        builder.setIcon(R.drawable.icon_update);
                        builder.setItems(arrType, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                edtType.setText(arrType[i]);
                            }
                        });
                        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

                builder.setTitle("Update Informatin");
                builder.setIcon(R.drawable.icon_update);
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TaskInfo inFo = new TaskInfo();
                        inFo.setId(Integer.parseInt(edtID.getText().toString().trim()));
                        inFo.setTitle(edtTitle.getText().toString().trim());
                        inFo.setContent(edtContent.getText().toString().trim());
                        inFo.setDate(edtDate.getText().toString().trim());
                        inFo.setType(edtType.getText().toString().trim());


                        long check = taskInfoDAO.updateInfo(inFo);
                        if(check < 0){
                            Toast.makeText(context.getApplicationContext(), "Update fail", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context.getApplicationContext(), "Update succesful", Toast.LENGTH_SHORT).show();
                        }

                        list.set(position,inFo);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderInfo extends RecyclerView.ViewHolder{
        TextView tvContent, tvDate;
        CheckBox chkTask;
        ImageView imgUpdate, getImgDelete;

        public ViewHolderInfo(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDate = itemView.findViewById(R.id.tvDay);
            chkTask = itemView.findViewById(R.id.chkTask);
            imgUpdate = itemView.findViewById(R.id.imgUpdate);
            getImgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
