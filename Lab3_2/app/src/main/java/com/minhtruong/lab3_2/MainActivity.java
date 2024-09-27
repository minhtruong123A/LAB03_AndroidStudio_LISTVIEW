package com.minhtruong.lab3_2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Item> itemList;
    private CustomAdapter adapter;
    private ListView listView;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btn_add);
        itemList = new ArrayList<>();
        itemList.add(new Item(R.drawable.banana, "Chuối tiêu", "Chuối tiêu Long An"));
        itemList.add(new Item(R.drawable.dragon_fruit, "Thanh Long", "Thanh long ruột đỏ"));
        itemList.add(new Item(R.drawable.strawberry, "Dâu tây", "Dâu tây Đà Lạt"));
        itemList.add(new Item(R.drawable.watermelon, "Dưa hấu", "Dưa hấu Tiền Giang"));
        itemList.add(new Item(R.drawable.orange, "Cam vàng", "Cam vàng nhập kẩu"));
        adapter = new CustomAdapter(this, itemList);
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            addItem();
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            editOrDeleteItem(position);
        });
    }

    private void addItem() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_edit_item, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Item mới");
        EditText editTitle = view.findViewById(R.id.editTitle);
        EditText editDescription = view.findViewById(R.id.editDescription);
        ImageView itemImageView = view.findViewById(R.id.itemImageView);
        final int[] selectedImageResId = {R.drawable.banana};
        itemImageView.setOnClickListener(v -> selectImageFromGallery(result -> {
            itemImageView.setImageResource(result);
            selectedImageResId[0] = result;
        }));

        builder.setView(view);
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();

            if (!title.isEmpty() && !description.isEmpty()) {
                itemList.add(new Item(selectedImageResId[0], title, description));
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void editOrDeleteItem(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        builder.setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
            if (which == 0) {
                editItem(position);
            } else if (which == 1) {
                deleteItem(position);
            }
        });
        builder.show();
    }

    private void editItem(int position) {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_edit_item, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa Item");
        Item currentItem = itemList.get(position);
        EditText editTitle = view.findViewById(R.id.editTitle);
        EditText editDescription = view.findViewById(R.id.editDescription);
        ImageView itemImageView = view.findViewById(R.id.itemImageView);
        editTitle.setText(currentItem.getTitle());
        editDescription.setText(currentItem.getDescription());
        itemImageView.setImageResource(currentItem.getImageResId());
        final int[] selectedImageResId = {currentItem.getImageResId()};
        itemImageView.setOnClickListener(v -> selectImageFromGallery(result -> {
            itemImageView.setImageResource(result);
            selectedImageResId[0] = result;
        }));

        builder.setView(view);
        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String updatedTitle = editTitle.getText().toString();
            String updatedDescription = editDescription.getText().toString();

            if (!updatedTitle.isEmpty() && !updatedDescription.isEmpty()) {
                currentItem.setTitle(updatedTitle);
                currentItem.setDescription(updatedDescription);
                currentItem.setImageResId(selectedImageResId[0]);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void selectImageFromGallery(OnImageSelectedListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn hình ảnh");

        int[] images = {R.drawable.banana, R.drawable.dragon_fruit, R.drawable.watermelon, R.drawable.orange, R.drawable.other};
        String[] imageNames = {"Banana", "Dragon Fruit", "Watermelon", "Orange", "Other topic"};

        builder.setItems(imageNames, (dialog, which) -> {
            listener.onImageSelected(images[which]);
        });
        builder.show();
    }

    interface OnImageSelectedListener {
        void onImageSelected(int imageResId);
    }

    private void deleteItem(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa Item")
                .setMessage("Bạn có chắc chắn muốn xóa Item này? Really?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    itemList.remove(position);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Item đã bị xóa!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
