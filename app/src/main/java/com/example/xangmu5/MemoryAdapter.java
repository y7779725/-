package com.example.xangmu5;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {

    private Context context;
    private List<MBdate> arr1;
    private sql sql;
    private SQLiteDatabase db;
    private OnItemClickListener onItemClickListener;

    // 定义接口
    public interface OnItemClickListener {
        void onItemClick(MBdate item);
    }

    // 设置监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    // 构造函数，接收数据列表
    public MemoryAdapter(Context context, List<MBdate> arr1) {
        this.context = context;
        this.arr1 = arr1;
        sql = new sql(context);
        db = sql.getWritableDatabase(); // 初始化数据库实例
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 通过LayoutInflater加载item布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recly, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MBdate mBdate = arr1.get(position);
        holder.title.setText(mBdate.getTitle());
        holder.content.setText(mBdate.getContent());
        holder.time.setText(mBdate.getMtime());

        // 处理 Bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(mBdate.getImgpath());
        if (bitmap != null) {
            holder.img.setImageBitmap(bitmap);
        }

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(10f);
        gradientDrawable.setColor(color);
        holder.recly.setBackground(gradientDrawable);

        // 设置点击监听
        if (holder.recly != null) {
            // 设置点击事件
            holder.recly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 在这里处理点击事件
                    // 例如，显示详情或删除该项
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setMessage("确定删除吗?");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.delete("tb_memory", "title=?", new String[]{mBdate.getTitle()});
                            arr1.remove(position);
                            notifyItemRemoved(position);
                            dialogInterface.dismiss();
                        }
                    });
                    dialog.setNegativeButton("取消", null);
                    dialog.setCancelable(false);
                    dialog.create().show();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        // 返回列表长度
        return arr1.size();
    }

    // 定义 ViewHolder 内部类
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content, time;
        ImageView img;
        LinearLayout recly;

        public ViewHolder(View itemView) {
            super(itemView);
            recly = itemView.findViewById(R.id.item_layout);
            title = itemView.findViewById(R.id.textView4);
            content = itemView.findViewById(R.id.textView5);
            img = itemView.findViewById(R.id.imageView2);
            time = itemView.findViewById(R.id.textView7);
        }
    }
}
