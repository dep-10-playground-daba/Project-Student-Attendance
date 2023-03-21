package lk.ijse.dep10.app;

import javafx.application.Application;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (DBConnection.getInstance().getConnecction() != null && !DBConnection.getInstance().getConnecction().isClosed())
                    DBConnection.getInstance().getConnecction().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        generateSchemaIfNotExist();
        System.exit(0);
    }


    private void generateSchemaIfNotExist() {
        try {
            Connection connection = DBConnection.getInstance().getConnecction();
            List<String> tableArray = new ArrayList<>();
            Statement stm1 = connection.createStatement();
            ResultSet rst = stm1.executeQuery("SHOW TABLES");

            Set tableNameSet = new HashSet<>();
//            DatabaseMetaData databaseMetaData = connection.getMetaData();
//            ResultSet resultSet = databaseMetaData.getTables("dep10_student_attendence", null, null, new String[]{"TABLE"});
            int count = 0;
            while (rst.next()) {
                count++;
                tableNameSet.add(rst.getString(1));
            }
//                String name = resultSet.getString("TABLE_NAME");
//                System.out.println(name + " Table is in database ");
            boolean tableExists = tableNameSet.containsAll(Set.of("Attendance", "Student", "Picture", "User"));
            if (!tableExists)stm1.execute(readDBScript());
//                tableArray.add(name);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String readDBScript() {
        StringBuilder dbScript = new StringBuilder();
//        Connection connection = DBConnection.getInstance().getConnecction();
//        Statement stm = null;
        try {
//            stm = connection.createStatement();
//            if (!(tableArray.contains("Attendance")&&tableArray.contains("Student")&&tableArray.contains("Picture")&&tableArray.contains("User"))){
        System.out.println("Schema is working on");

        InputStream is = getClass().getResourceAsStream("/schema.sql");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;

        while ((line = br.readLine()) != null) {
            dbScript.append(line).append("\n");
        }

//            stm.execute(dbScript.toString());
            br.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dbScript.toString();
    }
}


