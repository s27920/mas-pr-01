package App.Callbacks;

import App.Panels.Components.MissionMarker;

import java.awt.*;

@FunctionalInterface
public interface ShapeIntersectCallback {
    MissionMarker[] onShapeIntercept(Shape shape);
}
