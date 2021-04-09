package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ninjacode98.journal.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Journal;

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Journal> journalList;

    public JournalRecyclerAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public JournalRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_row,parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalRecyclerAdapter.ViewHolder holder, int position) {

        Journal journal = journalList.get(position);
        String imageUrl = journal.getImageURL();

        holder.title.setText(journal.getTitle());
        holder.thought.setText(journal.getThought());

        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_one)
                .fit()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView thought;
        public TextView dateAdded;
        public TextView name;
        public ImageView image;

        public String userId;
        public String userName;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.journal_title_list);
            thought = itemView.findViewById(R.id.journal_thought_list);
            dateAdded = itemView.findViewById(R.id.journal_time_list);
            image = itemView.findViewById(R.id.journal_image_list);
        }
    }
}
