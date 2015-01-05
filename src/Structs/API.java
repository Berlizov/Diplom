package Structs;

/**
 * Created by Eugene Berlizov on 17.10.2014.
 */
public enum API {
    UPDATE(0,UsersTypes.NO),
    LOGIN(2,UsersTypes.NO),
    ADD_USER(1,UsersTypes.ADMIN),
    CHANGE_USER_TYPE(1,UsersTypes.ADMIN),
    CHANGE_USER_PASS(1,UsersTypes.NO),
    GET_USERS_BY_TYPES(1,UsersTypes.NO),
    GET_ALL_USERS_AND_TYPES(0,UsersTypes.ADMIN),
    GET_PROJECTS(0,UsersTypes.NO),
    ADD_PROJECTS(2,UsersTypes.ADMIN),
    CHANGE_PROJECT_PRODUCT_OWNER(2,UsersTypes.ADMIN),
    GET_PROJECT_PRODUCT_OWNER(1,UsersTypes.NO),
    GET_PROJECT_USERS(1,UsersTypes.NO),
    GET_PROJECT_USERS_BY_TYPE(2,UsersTypes.NO),
    CHANGE_PROJECT_USERS(-1,UsersTypes.PRODUCT_OWNER),
    ADD_PROJECT_TASK(1,UsersTypes.PRODUCT_OWNER),
    GET_PROJECT_TASKS(1,UsersTypes.NO),
    SET_PROJECT_TASK_COMPLEXITY(1,UsersTypes.NO),
    GET_PROJECT_USER_TASKS_COMPLEXITY(1,UsersTypes.NO),
    CHANGE_TASK_USERS(-1,UsersTypes.PRODUCT_OWNER),
    GET_TASK_USERS(1,UsersTypes.PRODUCT_OWNER),
    ADD_TASK_SUBTASK(1,UsersTypes.PRODUCT_OWNER),
    GET_TASK_SUBTASKS(1,UsersTypes.NO),
    CHANGE_SUBTASKS_COMPLETENESS(1,UsersTypes.DEVELOPER),
    ADD_TASK_DESCRIPTION(2,UsersTypes.PRODUCT_OWNER),
    GET_TASK_DESCRIPTION(1,UsersTypes.NO),
    ADD_SPRINT(1,UsersTypes.PRODUCT_OWNER);

    final int argCount;
    final UsersTypes types;

    private API(int argCount, UsersTypes types) {
        this.argCount = argCount;
        this.types=types;
    }

    public int getArgCount() {
        return argCount;
    }
}
