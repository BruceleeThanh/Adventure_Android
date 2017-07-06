package studio.crazybt.adventure.listeners;

import java.util.List;

import studio.crazybt.adventure.models.Direction;

/**
 * Created by Brucelee Thanh on 07/07/2017.
 */

public interface OnDirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Direction> directions);
}
