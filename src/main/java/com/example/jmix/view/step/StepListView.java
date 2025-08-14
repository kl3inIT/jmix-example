package com.example.jmix.view.step;

import com.example.jmix.entity.Step;
import com.example.jmix.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.textfield.JmixBigDecimalField;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

@Route(value = "steps", layout = MainView.class)
@ViewController(id = "Step.list")
@ViewDescriptor(path = "step-list-view.xml")
@LookupComponent("stepsDataGrid")
@DialogMode(width = "64em")
public class StepListView extends StandardListView<Step> {


}