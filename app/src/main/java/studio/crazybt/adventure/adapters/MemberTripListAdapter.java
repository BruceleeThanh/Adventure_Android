package studio.crazybt.adventure.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.activities.ProfileActivity;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.models.TripMember;
import studio.crazybt.adventure.utils.SharedPref;

/**
 * Created by Brucelee Thanh on 02/10/2016.
 */

public class MemberTripListAdapter extends RecyclerView.Adapter<MemberTripListAdapter.ViewHolder> {

    private Context rootContext;
    private List<TripMember> lstTripMember;
    private String ownerTrip;
    private String currentUserId;

    public MemberTripListAdapter(Context rootContext, List<TripMember> lstTripMember, String ownerTrip) {
        this.rootContext = rootContext;
        this.lstTripMember = lstTripMember;
        this.ownerTrip = ownerTrip;
        currentUserId = SharedPref.getInstance(rootContext).getString(ApiConstants.KEY_ID, "");
    }

    public MemberTripListAdapter(Context rootContext) {
        this.rootContext = rootContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TripMember tripMember = lstTripMember.get(position);
        holder.tvProfileName.setText(tripMember.getOwner().getFirstName() + " " + tripMember.getOwner().getLastName());
        holder.tvProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContext, ProfileActivity.class);
                rootContext.startActivity(intent);
            }
        });
        if (ownerTrip.equals(tripMember.getOwner().getId())) {
            holder.tvPermission.setText(R.string.monitor_trip_member);
        } else {
            holder.tvPermission.setText(R.string.member_trip_member);
        }
        if (currentUserId.equals(tripMember.getOwner().getId())) {
            holder.btnCallMemberTrip.setVisibility(View.GONE);
            holder.btnMessageMemberTrip.setVisibility(View.GONE);
        }else{
            holder.btnCallMemberTrip.setVisibility(View.VISIBLE);
            holder.btnMessageMemberTrip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return lstTripMember == null ? 0 : lstTripMember.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.tvProfileName)
        TextView tvProfileName;
        @BindView(R.id.btnCallMemberTrip)
        Button btnCallMemberTrip;
        @BindView(R.id.btnMessageMemberTrip)
        Button btnMessageMemberTrip;
        @BindView(R.id.tvPermission)
        TextView tvPermission;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
