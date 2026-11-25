package database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @Test
    void shouldReturnFalseWhenSavingNullRecord() {
        DatabaseManager dbManager = new DatabaseManager();
        assertFalse(dbManager.saveRecord(null));
    }
}
