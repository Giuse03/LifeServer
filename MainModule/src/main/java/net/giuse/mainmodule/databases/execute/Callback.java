package net.giuse.mainmodule.databases.execute;

import java.sql.PreparedStatement;

public interface Callback {

    void setQuery(PreparedStatement preparedStatement);
}
