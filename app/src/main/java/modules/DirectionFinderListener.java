package modules;

import java.util.List;

/**
 * Created by ling on 2016/10/30.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
