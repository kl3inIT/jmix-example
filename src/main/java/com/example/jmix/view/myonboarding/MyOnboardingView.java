package com.example.jmix.view.myonboarding;


import com.example.jmix.entity.User;
import com.example.jmix.entity.UserStep;
import com.example.jmix.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Route(value = "my-onboarding-view", layout = MainView.class)
@ViewController(id = "MyOnboardingView")
@ViewDescriptor(path = "my-onboarding-view.xml")
public class MyOnboardingView extends StandardView {

    @ViewComponent
    private Span completedStepsLabel;

    @ViewComponent
    private Span overdueStepsLabel;

    @ViewComponent
    private Span totalStepsLabel;

    @ViewComponent
    private CollectionContainer<UserStep> userStepsDc;

    @ViewComponent
    private CollectionLoader<UserStep> userStepsDl;

    @ViewComponent
    private DataContext dataContext;

    @Autowired
    private UiComponents uiComponents;

    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        final User user = (User) currentAuthentication.getUser();
        userStepsDl.setParameter("user", user);
        userStepsDl.load();
        updateLabels();
    }

    @Subscribe(id = "userStepsDc", target = Target.DATA_CONTAINER)
    public void onUserStepsDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<UserStep> event) {
        updateLabels();
    }

    @Supply(to = "userStepsDataGrid.completed", subject = "renderer")
    private Renderer<UserStep> userStepsDataGridCompletedRenderer() {
        return new ComponentRenderer<>(userStep -> {
            Checkbox checkbox = uiComponents.create(Checkbox.class);
            checkbox.setValue(userStep.getCompletedDate() != null);
            checkbox.addValueChangeListener(e -> {
                if (userStep.getCompletedDate() == null) {
                    userStep.setCompletedDate(LocalDate.now());
                } else {
                    userStep.setCompletedDate(null);
                }
            });
            return checkbox;
        });
    }

    private void updateLabels() {
        totalStepsLabel.setText("Total steps: " + userStepsDc.getItems().size());

        long completedCount = userStepsDc.getItems().stream()
                .filter(us -> us.getCompletedDate() != null)
                .count();
        completedStepsLabel.setText("Completed steps: " + completedCount);

        long overdueCount = userStepsDc.getItems().stream()
                .filter(us -> isOverdue(us))
                .count();
        overdueStepsLabel.setText("Overdue steps: " + overdueCount);
    }

    private boolean isOverdue(UserStep us) {
        return us.getCompletedDate() == null
                && us.getDueDate() != null
                && us.getDueDate().isBefore(LocalDate.now());
    }

    @Subscribe(id = "saveButton", subject = "clickListener")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.save();
        close(StandardOutcome.SAVE);
    }

    @Subscribe(id = "discardButton", subject = "clickListener")
    public void onDiscardButtonClick(final ClickEvent<JmixButton> event) {
        close(StandardOutcome.DISCARD);
    }

    @Install(to = "userStepsDataGrid.dueDate", subject = "partNameGenerator")
    private String userStepsDataGridDueDatePartNameGenerator(final UserStep userStep) {
        return isOverdue(userStep) ? "overdue-step" : null;
    }
}