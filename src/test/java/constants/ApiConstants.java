package constants;

public final class ApiConstants {
    public static final String BASE_URI = "https://practice.expandtesting.com/notes/api";
    public static final String HEALTH_CHECK = "/health-check";

    // User endpoints
    public static final String REGISTER_USER = "/users/register";
    public static final String LOGIN_USER = "/users/login";
    public static final String GET_USER_PROFILE = "/users/profile";
    public static final String UPDATE_USER_PROFILE = "/users/profile";
    public static final String FORGOT_PASSWORD = "/users/forgot-password";
    public static final String PASSWORD_RESET_TOKEN = "/users/verify-reset-password-token";
    public static final String RESET_PASSWORD = "/users/reset-password";
    public static final String CHANGE_PASSWORD = "/users/change-password";
    public static final String LOGOUT_USER = "/users/logout";
    public static final String DELETE_ACCOUNT = "/users/delete-account";

    // Notes endpoints
    public static final String CREATE_NOTE = "/notes";
    public static final String GET_NOTES = "/notes";
    public static final String GET_NOTE_BY_ID = "/notes/";
    public static final String UPDATE_NOTE = "/notes/";
    public static final String COMPLETE_NOTE = "/notes/";
    public static final String DELETE_NOTE = "/notes/";

    // Base JSON Schemas
    public static final String BASE_SCHEMA = "schemas/base_response.json";

    // Notes JSON Schemas
    public static final String USER_REGISTER_SCHEMA = "schemas/user_register_response.json";
    public static final String USER_LOGIN_SCHEMA = "schemas/user_login_response.json";
    public static final String USER_GET_PROFILE_SCHEMA = "schemas/user_get_profile_response.json";
    public static final String USER_UPDATE_PROFILE_SCHEMA = "schemas/user_update_profile.json";

    // Notes JSON Schemas
    public static final String NOTE_CREATE_SCHEMA = "schemas/note_create_response.json";
    public static final String NOTE_GET_ALL_SCHEMA = "schemas/note_get_all_response.json";
    public static final String NOTE_GET_BY_ID_SCHEMA = "schemas/note_get_by_id_response.json";
    public static final String NOTE_UPDATE_SCHEMA = "schemas/note_update_response.json";
    public static final String NOTE_PATCH_STATUS_SCHEMA = "schemas/note_patch_status_response.json";
}
