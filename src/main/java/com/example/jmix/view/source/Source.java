package com.example.jmix.view.source;

import com.airbyte.api.models.shared.SourceResponse;
import com.example.jmix.entity.SourceDTO;
import com.example.jmix.service.AirbyteService;
import com.example.jmix.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.LoadContext;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.DataLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "Source", layout = MainView.class)
@ViewController("Source")
@ViewDescriptor("Source.xml")
public class Source extends StandardView {

    @ViewComponent
    private Span totalSourcesLabel;

    @ViewComponent
    private Span healthStatusLabel;

    @ViewComponent
    private CollectionContainer<SourceDTO> sourcesDc;

    @ViewComponent
    private DataLoader sourcesDl;

    @Autowired
    private AirbyteService airbyteService;

    @Autowired
    private Notifications notifications;

    @Autowired
    private UiComponents uiComponents;

    @Install(to = "sourcesDl", target = Target.DATA_LOADER)
    private List<SourceDTO> sourcesDlLoadDelegate(LoadContext<SourceDTO> loadContext) {
        List<SourceResponse> apiData = airbyteService.getSources();
        return apiData.stream()
                .map(src -> new SourceDTO(
                        src.name(),
                        src.sourceId(),
                        src.sourceType(),
                        src.workspaceId()
                ))
                .toList();
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        reloadAndUpdateUi();
    }

    @Subscribe(id = "refreshButton", subject = "clickListener")
    public void onRefreshButtonClick(ClickEvent<JmixButton> event) {
        reloadAndUpdateUi();
        notifications.create("Sources refreshed successfully")
                .withType(Notifications.Type.SUCCESS)
                .withPosition(Notification.Position.TOP_END)
                .show();
    }

    private void reloadAndUpdateUi() {
        try {
            sourcesDl.load();
            updateSourcesCount();
            updateHealthStatus(true);
        } catch (Exception ex) {
            updateHealthStatus(false);
            notifications.create("Failed to refresh sources: " + ex.getMessage())
                    .withType(Notifications.Type.ERROR)
                    .withPosition(Notification.Position.TOP_END)
                    .show();
        }
    }

    private void updateSourcesCount() {
        int count = sourcesDc.getItems().size();
        totalSourcesLabel.setText("Total Sources: " + count);
    }

    private void updateHealthStatus(boolean ok) {
        healthStatusLabel.setText(ok ? "Airbyte Status: OK" : "Airbyte Status: ERROR");
        healthStatusLabel.getStyle().set("color", ok ? "green" : "red");
        healthStatusLabel.getStyle().set("font-weight", "600");
    }

    @Supply(to = "sourcesDataGrid.sourceType", subject = "renderer")
    private Renderer<SourceDTO> sourceTypeRenderer() {
        return new ComponentRenderer<>(source -> {
            Span typeSpan = uiComponents.create(Span.class);
            typeSpan.setText(source.getSourceType());
            typeSpan.getStyle().setColor("blue");
            return typeSpan;
        });
    }

    @Supply(to = "sourcesDataGrid.workspaceId", subject = "renderer")
    private Renderer<SourceDTO> workspaceIdRenderer() {
        return new ComponentRenderer<>(source -> {
            Span workspaceSpan = uiComponents.create(Span.class);
            workspaceSpan.setText(source.getWorkspaceId());
            workspaceSpan.getStyle().setColor("gray");
            return workspaceSpan;
        });
    }

    @Supply(to = "sourcesDataGrid.sourceId", subject = "renderer")
    private Renderer<SourceDTO> sourceIdRenderer() {
        return new ComponentRenderer<>(source -> {
            Span idSpan = uiComponents.create(Span.class);
            idSpan.setText(source.getSourceId());
            idSpan.getStyle().setColor("purple");
            return idSpan;
        });
    }
}
