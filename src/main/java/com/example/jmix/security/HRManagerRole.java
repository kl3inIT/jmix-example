package com.example.jmix.security;

import com.example.jmix.entity.Department;
import com.example.jmix.entity.Step;
import com.example.jmix.entity.User;
import com.example.jmix.entity.UserStep;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "HR Manager", code = HRManagerRole.CODE, scope = "UI")
public interface HRManagerRole {
    String CODE = "h-r-manager";

    @MenuPolicy(menuIds = "User.list")
    @ViewPolicy(viewIds = "User.list")
    void screens();

    @EntityPolicy(entityClass = Department.class, actions = EntityPolicyAction.READ)
    void department();

    @EntityPolicy(entityClass = Step.class, actions = EntityPolicyAction.READ)
    void step();

    @EntityAttributePolicy(entityClass = User.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = User.class, actions = EntityPolicyAction.ALL)
    void user();

    @EntityAttributePolicy(entityClass = UserStep.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityPolicy(entityClass = UserStep.class, actions = EntityPolicyAction.ALL)
    void userStep();
}