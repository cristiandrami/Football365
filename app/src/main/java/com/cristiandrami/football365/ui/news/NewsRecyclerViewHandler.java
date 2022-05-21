package com.cristiandrami.football365.ui.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cristiandrami.football365.R;
import com.cristiandrami.football365.model.utilities.listener_commands.LinkOpenerOnBrowserCommandOnClick;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerViewHandler extends RecyclerView.Adapter<NewsRecyclerViewHandler.ViewHolder>  {

    public class ViewHolder extends RecyclerView.ViewHolder{


        private ImageView itemImage;
        private TextView itemTitle;
        private TextView itemDescription;
        private RelativeLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            itemImage= itemView.findViewById(R.id.news_image_recycler_view_item);
            itemTitle= itemView.findViewById(R.id.news_title_recycler_view_item);
            itemDescription= itemView.findViewById(R.id.news_description_recycler_view_item);
            itemLayout=itemView.findViewById(R.id.news_recycler_view_item_layout);
        }

        public void setData(String image, String title, String description, String link){

            itemTitle.setText(title);
            itemDescription.setText(description);
            if(image!=null && !image.trim().isEmpty()){
                Picasso.with(context).load(image).into(itemImage);
            }else{
                itemImage.setImageResource(R.drawable.news_item_icon);
            }
            setClickListenerOnItem(link);

        }

        private void setClickListenerOnItem(String link) {
            itemLayout.setOnClickListener(new LinkOpenerOnBrowserCommandOnClick(link, context));
        }


    }
    private List<NewsRecyclerViewItemModel> items ;
    private Context context;

    public NewsRecyclerViewHandler(List<NewsRecyclerViewItemModel> items, Context context) {
        this.context=context;
        this.items = items;
    }


    //This is the method in which i connect my layout recycler view to the class
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //I take the view of the layout recyclerview
        View recyclerView= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_view_item_layout, parent, false);

        return new ViewHolder(recyclerView);
    }




    //whit this method I can bind the items in the layout recycler view with the variables in the model class
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String image = items.get(position).getNewsImage();
        String title=items.get(position).getNewsTitle();
        String description=items.get(position).getNewsDescription();
        String link=items.get(position).getArticleLink();

        holder.setData(image, title, description, link);

    }


    @Override
    public int getItemCount() {
        return items.size();

    }


}