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
import com.cristiandrami.football365.model.news.News;
import com.cristiandrami.football365.model.utilities.UtilitiesNumbers;
import com.cristiandrami.football365.model.utilities.UtilitiesStrings;
import com.cristiandrami.football365.model.utilities.listener_commands.LinkOpenerOnBrowserCommandOnClick;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsRecyclerViewHandler extends RecyclerView.Adapter<NewsRecyclerViewHandler.ViewHolder> {

    private final List<NewsRecyclerViewItemModel> items;
    private final Context context;

    public NewsRecyclerViewHandler(List<NewsRecyclerViewItemModel> items, Context context) {
        this.context = context;
        this.items = items;

    }

    //This is the method in which i connect my layout recycler view to the class
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //I take the view of the layout recyclerview
        View recyclerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_view_item_layout, parent, false);

        return new ViewHolder(recyclerView);
    }

    //whit this method I can bind the items in the layout recycler view with the variables in the model class
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String image = items.get(position).getImage();
        String title = items.get(position).getTitle();
        String description = items.get(position).getDescription();
        String link = items.get(position).getLink();
        int type = items.get(position).getType();

        holder.setData(image, title, description, link, type);

    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private final ImageView itemImage;
        private final TextView itemTitle;
        private final TextView itemDescription;
        private final RelativeLayout itemLayout;
        private final View likedArticleView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            itemImage = itemView.findViewById(R.id.news_image_recycler_view_item);
            itemTitle = itemView.findViewById(R.id.news_title_recycler_view_item);
            itemDescription = itemView.findViewById(R.id.news_description_recycler_view_item);
            itemLayout = itemView.findViewById(R.id.news_recycler_view_item_layout);
            likedArticleView = itemView.findViewById(R.id.news_fragment_liked_news_view);
        }

        public void setData(String image, String title, String description, String link, int type) {

            itemTitle.setText(title);
            itemDescription.setText(description);
            if (image != null && !image.trim().isEmpty()) {
                Picasso.with(context).load(image).into(itemImage);
            } else {
                itemImage.setImageResource(R.drawable.news_item_icon);
            }
            setClickListenerOnItem(link);
            if(type==UtilitiesNumbers.LIKED_NEWS_TYPE)
                likedArticleView.setBackground(context.getDrawable(R.drawable.ic_baseline_star_24));

            setClickListenerOnLikedView(image, title, description, link, type);

        }

        private void setClickListenerOnItem(String link) {
            itemLayout.setOnClickListener(new LinkOpenerOnBrowserCommandOnClick(link, context));
        }

        private void setClickListenerOnLikedView(String image, String title, String description, String link, int type) {
            likedArticleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    News currentNew = new News();
                    currentNew.setDescription(description);
                    currentNew.setImage(image);
                    currentNew.setLink(link);
                    currentNew.setTitle(title);
                    String newJSON = new Gson().toJson(currentNew);
                    if(type== UtilitiesNumbers.LIKED_NEWS_TYPE){
                        items.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), items.size());
                        FirebaseFirestore.getInstance().collection(UtilitiesStrings.FIREBASE_LIKED_NEWS_USERS_COLLECTION).
                                document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection(UtilitiesStrings.FIREBASE_LIKED_NEWS_SINGLE_USER_COLLECTION).
                                document(title).delete();
                        if(items.size()==0){

                        }
                    }else{
                        if (likedArticleView.getBackground().getConstantState().equals(context.getDrawable(R.drawable.ic_baseline_star_24).getConstantState())) {
                            likedArticleView.setBackground(context.getDrawable(R.drawable.ic_baseline_star_border_24));
                            FirebaseFirestore.getInstance().collection(UtilitiesStrings.FIREBASE_LIKED_NEWS_USERS_COLLECTION).
                                    document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection(UtilitiesStrings.FIREBASE_LIKED_NEWS_SINGLE_USER_COLLECTION).
                                    document(title).delete();
                        } else {
                            likedArticleView.setBackground(context.getDrawable(R.drawable.ic_baseline_star_24));
                            Map<String, Object> newToStore = new HashMap<>();
                            newToStore.put(UtilitiesStrings.LIKED_NEWS_COLUMN_NEWS_NAME_FIRESTORE, newJSON);
                            FirebaseFirestore.getInstance().collection(UtilitiesStrings.FIREBASE_LIKED_NEWS_USERS_COLLECTION).
                                    document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection(UtilitiesStrings.FIREBASE_LIKED_NEWS_SINGLE_USER_COLLECTION)
                                    .document(title).set(newToStore);
                        }
                    }


                }
            });
        }


    }


}