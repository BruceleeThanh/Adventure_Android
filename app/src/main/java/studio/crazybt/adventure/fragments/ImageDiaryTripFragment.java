package studio.crazybt.adventure.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.ImageStatusDetailListAdapter;
import studio.crazybt.adventure.models.ImageContent;
import studio.crazybt.adventure.models.TripDiary;

/**
 * Created by Brucelee Thanh on 18/03/2017.
 */

public class ImageDiaryTripFragment extends Fragment {

    private View rootView;
    @BindView(R.id.rvImageDiaryTrip)
    RecyclerView rvImageDiaryTrip;

    private List<ImageContent> lstImageContents;
    private ImageStatusDetailListAdapter imageDiaryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.fragment_image_diary_trip, container, false);
        }
        ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            TripDiary tripDiary = getArguments().getParcelable("data");
            lstImageContents = tripDiary.getImages();
            initImageDiaryTripList();
        }
        return rootView;
    }

    private void initImageDiaryTripList(){
        imageDiaryAdapter=new ImageStatusDetailListAdapter(getContext(), lstImageContents);
        rvImageDiaryTrip.setLayoutManager(new LinearLayoutManager(getContext()));
        rvImageDiaryTrip.setAdapter(imageDiaryAdapter);
    }
}
