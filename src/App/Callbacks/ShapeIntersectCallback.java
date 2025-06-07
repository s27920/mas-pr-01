package App.Callbacks;

import App.Panels.MissionMarker;

import java.awt.*;

@FunctionalInterface
public interface ShapeIntersectCallback {
    MissionMarker[] onShapeIntercept(Shape shape);
}
