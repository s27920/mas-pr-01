package Models.Util;

import Models.Panels.MissionMarker;

import java.awt.*;

@FunctionalInterface
public interface ShapeIntersectCallback {
    MissionMarker[] onShapeIntercept(Shape shape);
}
