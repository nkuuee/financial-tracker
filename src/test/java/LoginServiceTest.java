import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.LoginService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginServiceTest {

    @Mock
    private Connection mokConnection;

    @Mock
    private PreparedStatement mokPreparedStatement;

    @Mock
    private ResultSet mokResultSet;

    private LoginService loginService;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        when(mokConnection.prepareStatement(any(String.class))).thenReturn(mokPreparedStatement);
        when(mokPreparedStatement.executeQuery()).thenReturn(mokResultSet);

        loginService = new LoginService();
    }

    @Test
    public void testUserAuthentication() throws SQLException {
        // Сценарий с правильным паролем и юзернеймом
        when(mokResultSet.next()).thenReturn(true); // Пользователь найден
        when(mokResultSet.getString("password"))
                .thenReturn("correctPassword"); // Правильный пароль
        boolean result = loginService.userAuthentication("validUsername",
                "correctPassword");
        assertTrue(result);

        // Сценарий с неправильным паролем, но правильным юзернеймом
        when(mokResultSet.next()).thenReturn(true); // Пользователь найден
        when(mokResultSet.getString("password"))
                .thenReturn("wrongPassword"); // Неправильный пароль
        result = loginService.userAuthentication("validUsername", "wrongPassword");
        assertFalse(result);

        // Сценарий, когда пользователь не найден
        when(mokResultSet.next()).thenReturn(false); // Пользователь не найден
        result = loginService.userAuthentication("nonUsername", "wrongPassword");
        assertFalse(result);

        //Проверкас
        verify(mokPreparedStatement).setString(1, "validUsername");
        verify(mokPreparedStatement).executeQuery();
    }
}
