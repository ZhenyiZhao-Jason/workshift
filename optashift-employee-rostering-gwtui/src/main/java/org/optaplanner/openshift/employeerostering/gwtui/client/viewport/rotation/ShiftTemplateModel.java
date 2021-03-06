package org.optaplanner.openshift.employeerostering.gwtui.client.viewport.rotation;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.optaplanner.openshift.employeerostering.gwtui.client.viewport.grid.GridObject;
import org.optaplanner.openshift.employeerostering.gwtui.client.viewport.grid.HasGridObjects;
import org.optaplanner.openshift.employeerostering.shared.rotation.view.ShiftTemplateView;

public class ShiftTemplateModel implements HasGridObjects<LocalDateTime, RotationMetadata> {

    @Inject
    private ShiftTemplateGridObject earilerTwin;
    @Inject
    private ShiftTemplateGridObject laterTwin;

    private ShiftTemplateView shiftTemplateView;

    @Override
    public Long getId() {
        return shiftTemplateView.getId();
    }

    @Override
    public Collection<GridObject<LocalDateTime, RotationMetadata>> getGridObjects() {
        return Arrays.asList(earilerTwin, laterTwin);
    }

    public ShiftTemplateModel withShiftTemplateView(ShiftTemplateView shiftTemplateView) {
        this.shiftTemplateView = shiftTemplateView;
        earilerTwin.withShiftTemplateModel(this);
        laterTwin.withShiftTemplateModel(this);
        return this;
    }

    public ShiftTemplateView getShiftTemplateView() {
        return shiftTemplateView;
    }

    public boolean isLaterTwin(ShiftTemplateGridObject view) {
        return laterTwin == view;
    }

    public void setLaterTwin(ShiftTemplateGridObject view) {
        if (laterTwin != view) {
            earilerTwin = laterTwin;
            laterTwin = view;
        }
    }

    public void setEarilerTwin(ShiftTemplateGridObject view) {
        if (earilerTwin != view) {
            laterTwin = earilerTwin;
            earilerTwin = view;
        }
    }

    public void refreshTwin(ShiftTemplateGridObject view) {
        if (view == earilerTwin) {
            laterTwin.updateStartDateTimeWithoutRefresh(view.getStartPositionInScaleUnits().plusDays(view.getDaysInRotation()));
            laterTwin.updateEndDateTimeWithoutRefresh(view.getEndPositionInScaleUnits().plusDays(view.getDaysInRotation()));
            laterTwin.reposition();
        } else {
            earilerTwin.updateStartDateTimeWithoutRefresh(view.getStartPositionInScaleUnits().minusDays(view.getDaysInRotation()));
            earilerTwin.updateEndDateTimeWithoutRefresh(view.getEndPositionInScaleUnits().minusDays(view.getDaysInRotation()));
            earilerTwin.reposition();
        }
    }
}
