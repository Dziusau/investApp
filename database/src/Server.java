import com.example.myapp.Active;
import com.example.myapp.Operation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

public class Server {

    //region Nested types

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private Connection connection;

        public ClientHandler(Socket socket, Connection connection) {
            clientSocket = socket;
            this.connection = connection;
        }

        public void run() {
            ObjectInputStream clientInput;
            ObjectOutputStream clientOutput;

            try
            {
                String receivedMessage;
                String[] words;
                String answer = "";
                ArrayList<Object> saved = new ArrayList<>();
                boolean isString = true;


                clientInput = new ObjectInputStream(clientSocket.getInputStream());
                clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());

                receivedMessage = (String) clientInput.readObject();

                Log("-------------------------------------------------");
                Log(String.format("Message received: %s", receivedMessage));

                words = receivedMessage.split("/");

                //logic

                switch (words[0]) {

                    case "reg" -> {
                        ResultSet resultSet;

                        //region user
                        if (words[1].equals("1")) {
                            PreparedStatement statement = connection.prepareStatement(
                                    "SELECT * FROM Пользователь WHERE Email = ?"
                            );
                            statement.setString(1, words[4]);
                            resultSet = statement.executeQuery();

                            if (resultSet.next()) {
                                answer = "fail";
                            } else {
                                statement = connection.prepareStatement(
                                        "INSERT INTO Пользователь (UserName, UserPassword, Email) Values (?, ?, ?)"
                                );
                                statement.setString(1, words[2]);
                                statement.setString(2, words[3]);
                                statement.setString(3, words[4]);
                                statement.executeUpdate();

                                answer = "success";
                            }
                        }
                        //endregion
                        //region admin
                        else {
                            PreparedStatement statement = connection.prepareStatement(
                                    "SELECT * FROM Админ WHERE AdminName = ?"
                            );
                            statement.setString(1, words[2]);
                            resultSet = statement.executeQuery();

                            if (resultSet.next()) {
                                answer = "fail";
                            } else {
                                statement = connection.prepareStatement(
                                        "INSERT INTO Админ (AdminName, AdminPassword) Values (?, ?)"
                                );
                                statement.setString(1, words[2]);
                                statement.setString(2, words[3]);
                                statement.executeUpdate();

                                answer = "success";
                            }
                        }
                        //endregion
                    }
                    case "ent" -> {
                        ResultSet resultSet;

                        //region user
                        if (words[1].equals("1")) {
                            PreparedStatement statement = connection.prepareStatement(
                                    "SELECT * FROM Пользователь WHERE Email = ?"
                            );
                            statement.setString(1, words[2]);
                            resultSet = statement.executeQuery();

                            if (resultSet.next() &&
                                    (Objects.equals(resultSet.getString("UserPassword"), words[3]))) {
                                answer = "success";
                            } else {
                                answer = "fail";
                            }
                        }
                        //endregion
                        //region admin
                        else {
                            PreparedStatement statement = connection.prepareStatement(
                                    "SELECT * FROM Админ WHERE AdminName = ?"
                            );
                            statement.setString(1, words[2]);
                            resultSet = statement.executeQuery();

                            if (resultSet.next() &&
                                    (Objects.equals(resultSet.getString("AdminPassword"), words[3]))) {
                                answer = "success";
                            } else {
                                answer = "fail";
                            }
                        }
                        //endregion
                    }

                    //region User operations
                    case "viewUser" -> {
                        ResultSet resultSet;

                        PreparedStatement statement = connection.prepareStatement(
                                "SELECT * FROM Пользователь WHERE Email = ?"
                        );
                        statement.setString(1, words[1]);

                        resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                            answer = resultSet.getString("UserName") + '/' +
                                    resultSet.getString("UserPassword");
                        }
                        else {
                            answer = "fail";
                        }
                    }
                    case "editUser" -> {
                        PreparedStatement statement;

                        //region changePassword
                        if (words[2].equals("")){
                            statement = connection.prepareStatement(
                                    "UPDATE Пользователь SET UserPassword = ? WHERE Email = ?"
                            );
                            statement.setString(1, words[3]);
                            statement.setString(2, words[1]);
                        }
                        //endregion
                        //region changeName
                        else if (words[3].equals(".")) {
                            statement = connection.prepareStatement(
                                    "UPDATE Пользователь SET UserName = ? WHERE Email = ?"
                            );
                            statement.setString(1, words[2]);
                            statement.setString(2, words[1]);
                        }
                        //endregion
                        //region changeNamePassword
                        else {
                            statement = connection.prepareStatement(
                                    "UPDATE Пользователь SET UserName = ?, UserPassword = ? WHERE Email = ?"
                            );
                            statement.setString(1, words[2]);
                            statement.setString(2, words[3]);
                            statement.setString(3, words[1]);
                        }
                        //endregion

                        statement.executeUpdate();

                        answer = "success";
                    }
                    case "deleteUser" -> {
                        PreparedStatement statement = connection.prepareStatement(
                                "DELETE FROM Пользователь WHERE Email = ?"
                        );
                        statement.setString(1, words[1]);
                        statement.executeUpdate();

                        answer = "success";
                    }

                    case "addCash" -> {
                        PreparedStatement statement = connection.prepareStatement(
                          "SELECT Balance FROM Пользователь WHERE Email = ?"
                        );
                        statement.setString(1, words[1]);

                        ResultSet resultSet = statement.executeQuery();

                        resultSet.next();
                        int cash = resultSet.getInt("Balance");

                        statement = connection.prepareStatement(
                                "UPDATE Пользователь SET Balance = ? WHERE Email = ?"
                        );
                        statement.setInt(1, cash + Integer.parseInt(words[2]));
                        statement.setString(2, words[1]);

                        statement.executeUpdate();

                        answer = "success";
                    }
                    case "viewCash" -> {
                        ResultSet resultSet;

                        PreparedStatement statement = connection.prepareStatement(
                                "SELECT * FROM Пользователь WHERE Email = ?"
                        );
                        statement.setString(1, words[1]);

                        resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                            answer += resultSet.getInt("Balance");
                        }
                        else {
                            answer = "fail";
                        }
                    }
                    case "viewActiveUser" -> {
                        isString = false;

                        ResultSet resultSet = connection.createStatement().executeQuery(
                                "SELECT Актив.ActiveName, Актив.ActiveType, Котировка.Price " +
                                        "FROM Котировка " +
                                        "JOIN Актив " +
                                            "ON Актив.ActiveID = Котировка.ActiveID " +
                                            "AND Актив.ActiveAccess = 'доступен'" +
                                        "WHERE Котировка.QuotationDate = " +
                                            "(SELECT MAX(QuotationDate) " +
                                            "FROM Котировка " +
                                            "WHERE Котировка.ActiveID = Актив.ActiveID)" +
                                        "ORDER BY Актив.ActiveName"
                        );

                        while (resultSet.next()) {
                            Active active = new Active(resultSet.getString(1), resultSet.getString(2), resultSet.getInt(3));
                            saved.add(active);
                        }
                    }
                    case "buyActive" -> {
                        ResultSet resultSet_user, resultSet_active;

                        //region SELECT * FROM Пользователь
                        PreparedStatement statement_user = connection.prepareStatement(
                                "SELECT * FROM Пользователь WHERE Email = ?"
                        );
                        statement_user.setString(1, words[1]);

                        resultSet_user = statement_user.executeQuery();
                        resultSet_user.next();
                        //endregion

                        //region SELECT Price, ActiveID FROM Котировка
                        PreparedStatement statement_active = connection.prepareStatement(
                                "SELECT Котировка.Price, Актив.ActiveID " +
                                        "FROM Котировка " +
                                        "JOIN Актив " +
                                            "ON Актив.ActiveID = Котировка.ActiveID " +
                                            "AND Актив.ActiveName = ?" +
                                        "WHERE Котировка.QuotationDate = " +
                                            "(SELECT MAX(QuotationDate) " +
                                            "FROM Котировка " +
                                            "WHERE Котировка.ActiveID = Актив.ActiveID)"
                        );
                        statement_active.setString(1, words[2]);

                        resultSet_active = statement_active.executeQuery();
                        resultSet_active.next();
                        //endregion

                        int balance  = resultSet_user.getInt("Balance");
                        String userID = resultSet_user.getString("UserID");

                        int cost = resultSet_active.getInt(1) * Integer.parseInt(words[3]);
                        String activeID = resultSet_active.getString(2);

                        if (balance < cost) {
                            answer = "fail";

                            //region INSERT INTO Операция
                            PreparedStatement statement_operation = connection.prepareStatement(
                                    "INSERT INTO Операция " +
                                            "(ActiveID, UserID, Amount, OperationDate, OperationType, IsSuccess) " +
                                            "VALUES (?, ?, ?, NOW(), 'покупка', 'отклонено')"
                            );
                            statement_operation.setString(1, activeID);
                            statement_operation.setString(2, userID);
                            statement_operation.setInt(3, Integer.parseInt(words[3]));

                            statement_operation.execute();
                            //endregion
                        }
                        else {
                            answer = "success";

                            //region INSERT INTO Операция
                            PreparedStatement statement_operation = connection.prepareStatement(
                                    "INSERT INTO Операция " +
                                            "(ActiveID, UserID, Amount, OperationDate, OperationType, IsSuccess) " +
                                            "VALUES (?, ?, ?, NOW(), 'покупка', 'успешно')"
                            );
                            statement_operation.setString(1, activeID);
                            statement_operation.setString(2, userID);
                            statement_operation.setInt(3, Integer.parseInt(words[3]));

                            statement_operation.execute();
                            //endregion

                            //region UPDATE Пользователь SET Balance
                            PreparedStatement statement = connection.prepareStatement(
                                    "UPDATE Пользователь SET Balance = ? WHERE UserID = ?"
                            );
                            statement.setInt(1, balance - cost);
                            statement.setString(2, userID);

                            statement.executeUpdate();
                            //endregion
                        }
                    }

                    case "viewPortfolioUser" -> {
                        isString = false;

                        //region SELECT ActiveName, ActiveType, Price FROM Portfolio
                        ResultSet resultSet = connection.createStatement().executeQuery(
                                "SELECT Актив.ActiveName, Актив.ActiveType, Котировка.Price " +
                                        "FROM Портфель " +
                                        "JOIN Операция " +
                                            "ON Операция.OperationID = Портфель.OperationID " +
                                        "JOIN Актив " +
                                            "ON Актив.ActiveID = Операция.ActiveID " +
                                        "JOIN Котировка " +
                                            "ON Актив.ActiveID = Котировка.ActiveID " +
                                        "WHERE Котировка.QuotationDate = " +
                                            "(SELECT MAX(QuotationDate) " +
                                            "FROM Котировка " +
                                            "WHERE Котировка.ActiveID = Актив.ActiveID) " +
                                        "GROUP BY Актив.ActiveName " +
                                        "ORDER BY Актив.ActiveName"
                        );
                        //endregion

                        //region SELECT SUM(Amount) FROM Portfolio WHERE 'buy'
                        ResultSet resultSet_amountBuy = connection.createStatement().executeQuery(
                                "SELECT Актив.ActiveName, SUM(Операция.Amount) " +
                                        "FROM Портфель " +
                                        "JOIN Операция " +
                                            "ON Операция.OperationID = Портфель.OperationID " +
                                        "JOIN Актив " +
                                            "ON Актив.ActiveID = Операция.ActiveID " +
                                        "WHERE Операция.OperationType = 'покупка' " +
                                        "GROUP BY Актив.ActiveName " +
                                        "ORDER BY Актив.ActiveName"
                        );
                        //endregion

                        // region SELECT SUM(Amount) FROM Portfolio WHERE 'sell'
                        ResultSet resultSet_amountSell = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(
                                "SELECT Актив.ActiveName, SUM(Операция.Amount) " +
                                        "FROM Портфель " +
                                        "JOIN Операция " +
                                            "ON Операция.OperationID = Портфель.OperationID " +
                                        "JOIN Актив " +
                                            "ON Актив.ActiveID = Операция.ActiveID " +
                                        "WHERE Операция.OperationType = 'продажа' " +
                                        "GROUP BY Актив.ActiveName " +
                                        "ORDER BY Актив.ActiveName"
                        );
                        //endregion

                        while (resultSet.next()) {
                            int amount = 0;
                            resultSet_amountBuy.next();

                            boolean isFound = false;
                            while (resultSet_amountSell.next()) {
                                String name_buy = resultSet_amountBuy.getString(1);
                                String name_sell = resultSet_amountSell.getString(1);

                                if (Objects.equals(name_buy, name_sell)) {
                                    amount = resultSet_amountBuy.getInt(2) - resultSet_amountSell.getInt(2);
                                    isFound = true;
                                    break;
                                }
                            }
                            if (!isFound) amount = resultSet_amountBuy.getInt(2);
                            resultSet_amountSell.beforeFirst();


                            if (amount != 0) {
                                Active active = new Active(resultSet.getString(1),
                                        resultSet.getString(2),
                                        resultSet.getInt(3),
                                        amount
                                );
                                saved.add(active);
                            }
                        }
                    }
                    case "sellActive" -> {
                        ResultSet resultSet_user, resultSet_active;

                        //region SELECT UserID, Balance FROM Пользователь
                        PreparedStatement statement_user = connection.prepareStatement(
                                "SELECT UserID, Balance FROM Пользователь WHERE Email = ?"
                        );
                        statement_user.setString(1, words[1]);

                        resultSet_user = statement_user.executeQuery();
                        resultSet_user.next();
                        //endregion

                        //region SELECT Price, ActiveID FROM Котировка
                        PreparedStatement statement_active = connection.prepareStatement(
                                "SELECT Котировка.Price, Актив.ActiveID, Операция.Amount " +
                                        "FROM Котировка " +
                                        "JOIN Актив " +
                                        "ON Актив.ActiveID = Котировка.ActiveID " +
                                        "AND Актив.ActiveName = ? " +
                                        "JOIN Операция " +
                                        "ON Актив.ActiveID = Операция.ActiveID " +
                                        "WHERE Котировка.QuotationDate = " +
                                            "(SELECT MAX(QuotationDate) " +
                                            "FROM Котировка " +
                                            "WHERE Котировка.ActiveID = Актив.ActiveID)"
                        );
                        statement_active.setString(1, words[2]);

                        resultSet_active = statement_active.executeQuery();
                        resultSet_active.next();
                        //endregion

                        int balance = resultSet_user.getInt("Balance");
                        String userID = resultSet_user.getString("UserID");

                        int cost = resultSet_active.getInt(1) * Integer.parseInt(words[3]);
                        String activeID = resultSet_active.getString(2);

                        //region INSERT INTO Операция
                        PreparedStatement statement_operation = connection.prepareStatement(
                                "INSERT INTO Операция " +
                                        "(ActiveID, UserID, Amount, OperationDate, OperationType, IsSuccess) " +
                                        "VALUES (?, ?, ?, NOW(), 'продажа', 'успешно')"
                        );
                        statement_operation.setString(1, activeID);
                        statement_operation.setString(2, userID);
                        statement_operation.setInt(3, Integer.parseInt(words[3]));

                        statement_operation.execute();
                        //endregion

                        //region UPDATE Пользователь SET Balance
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE Пользователь SET Balance = ? WHERE UserID = ?"
                        );
                        statement.setInt(1, balance + cost);
                        statement.setString(2, userID);

                        statement.executeUpdate();
                        //endregion

                        answer = "success";
                    }
                    //endregion

                    //region Admin operations
                    case "viewAdmin" -> {
                        ResultSet resultSet;

                        PreparedStatement statement = connection.prepareStatement(
                                "SELECT * FROM Админ WHERE AdminName = ?"
                        );
                        statement.setString(1, words[1]);

                        resultSet = statement.executeQuery();

                        if (resultSet.next()) {
                            answer = resultSet.getString("AdminPassword");
                        }
                        else {
                            answer = "fail";
                        }
                    }
                    case "editAdmin" -> {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE Админ SET AdminPassword = ? WHERE AdminName = ?"
                        );;

                        statement.setString(1, words[2]);
                        statement.setString(2, words[1]);

                        statement.executeUpdate();

                        answer = "success";
                    }
                    case "deleteAdmin" -> {
                        PreparedStatement statement = connection.prepareStatement(
                                "DELETE FROM Админ WHERE AdminName = ?"
                        );
                        statement.setString(1, words[1]);
                        statement.executeUpdate();

                        answer = "success";
                    }

                    case "viewActiveAdmin" -> {
                        isString = false;

                        ResultSet resultSet = connection.createStatement().executeQuery(
                                "SELECT Актив.ActiveName, Актив.ActiveType, Котировка.Price, Актив.ActiveAccess " +
                                        "FROM Котировка " +
                                        "JOIN Актив " +
                                        "ON Актив.ActiveID = Котировка.ActiveID " +
                                        "WHERE Котировка.QuotationDate = " +
                                            "(SELECT MAX(QuotationDate) " +
                                            "FROM Котировка " +
                                            "WHERE Котировка.ActiveID = Актив.ActiveID)" +
                                        "ORDER BY Актив.ActiveName"
                        );

                        while (resultSet.next()) {
                            Active active = new Active(resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getInt(3),
                                    resultSet.getString(4)
                            );
                            saved.add(active);
                        }
                    }
                    case "closeActive" -> {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE Актив SET ActiveAccess = 'не доступен' WHERE ActiveName = ?"
                        );
                        statement.setString(1, words[1]);
                        statement.executeUpdate();

                        answer = "success";
                    }
                    case "openActive" -> {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE Актив SET ActiveAccess = 'доступен' WHERE ActiveName = ?"
                        );
                        statement.setString(1, words[1]);
                        statement.executeUpdate();

                        answer = "success";
                    }
                    case "addCostActive" -> {
                        ResultSet resultSet_active;

                        //region SELECT ActiveID FROM Актив
                        PreparedStatement statement_active = connection.prepareStatement(
                                "SELECT ActiveID " +
                                        "FROM Актив " +
                                        "WHERE ActiveName = ?"
                        );
                        statement_active.setString(1, words[1]);

                        resultSet_active = statement_active.executeQuery();
                        resultSet_active.next();
                        //endregion

                        //region INSERT INTO Котировка
                        PreparedStatement statement = connection.prepareStatement(
                                "INSERT INTO Котировка " +
                                        "(ActiveID, QuotationDate, Price) " +
                                        "VALUES (?, NOW(), ?)"
                        );
                        statement.setString(1, resultSet_active.getString(1));
                        statement.setInt(2, Integer.parseInt(words[2]));

                        statement.executeUpdate();
                        //endregion

                        answer = "success";
                    }

                    case "viewOperations" -> {
                        isString = false;

                        ResultSet resultSet = null;
                        switch (words[1]){
                            case "0" -> {
                                resultSet = connection.createStatement().executeQuery(
                                        "SELECT Актив.ActiveName, Пользователь.UserName, Операция.Amount, Операция.OperationType, Операция.IsSuccess, Операция.OperationDate " +
                                                "FROM Операция " +
                                                "JOIN Актив " +
                                                "ON Актив.ActiveID = Операция.ActiveID " +
                                                "JOIN Пользователь " +
                                                "ON Пользователь.UserID = Операция.UserID " +
                                                "ORDER BY Актив.ActiveName"
                                );
                            }
                            case "1" -> {
                                resultSet = connection.createStatement().executeQuery(
                                        "SELECT Актив.ActiveName, Пользователь.UserName, Операция.Amount, Операция.OperationType, Операция.IsSuccess, Операция.OperationDate " +
                                                "FROM Операция " +
                                                "JOIN Актив " +
                                                "ON Актив.ActiveID = Операция.ActiveID " +
                                                "JOIN Пользователь " +
                                                "ON Пользователь.UserID = Операция.UserID " +
                                                "WHERE Операция.IsSuccess = 'успешно' " +
                                                "ORDER BY Актив.ActiveName"
                                );
                            }
                            case "2" -> {
                                resultSet = connection.createStatement().executeQuery(
                                        "SELECT Актив.ActiveName, Пользователь.UserName, Операция.Amount, Операция.OperationType, Операция.IsSuccess, Операция.OperationDate " +
                                                "FROM Операция " +
                                                "JOIN Актив " +
                                                "ON Актив.ActiveID = Операция.ActiveID " +
                                                "JOIN Пользователь " +
                                                "ON Пользователь.UserID = Операция.UserID " +
                                                "WHERE Операция.IsSuccess = 'отклонено' " +
                                                "ORDER BY Актив.ActiveName"
                                );
                            }
                            case "3" -> {
                                resultSet = connection.createStatement().executeQuery(
                                        "SELECT Актив.ActiveName, Пользователь.UserName, Операция.Amount, Операция.OperationType, Операция.IsSuccess, Операция.OperationDate " +
                                                "FROM Операция " +
                                                "JOIN Актив " +
                                                "ON Актив.ActiveID = Операция.ActiveID " +
                                                "JOIN Пользователь " +
                                                "ON Пользователь.UserID = Операция.UserID " +
                                                "WHERE Операция.OperationType = 'покупка' " +
                                                "ORDER BY Актив.ActiveName"
                                );
                            }
                            case "4" -> {
                                resultSet = connection.createStatement().executeQuery(
                                        "SELECT Актив.ActiveName, Пользователь.UserName, Операция.Amount, Операция.OperationType, Операция.IsSuccess, Операция.OperationDate " +
                                                "FROM Операция " +
                                                "JOIN Актив " +
                                                "ON Актив.ActiveID = Операция.ActiveID " +
                                                "JOIN Пользователь " +
                                                "ON Пользователь.UserID = Операция.UserID " +
                                                "WHERE Операция.OperationType = 'продажа' " +
                                                "ORDER BY Актив.ActiveName"
                                );
                            }
                            case "5" -> {
                                resultSet = connection.createStatement().executeQuery(
                                        "SELECT Актив.ActiveName, Пользователь.UserName, Операция.Amount, Операция.OperationType, Операция.IsSuccess, Операция.OperationDate " +
                                                "FROM Операция " +
                                                "JOIN Актив " +
                                                "ON Актив.ActiveID = Операция.ActiveID " +
                                                "JOIN Пользователь " +
                                                "ON Пользователь.UserID = Операция.UserID " +
                                                "ORDER BY Операция.OperationDate"
                                );
                            }
                            case "6" -> {
                                resultSet = connection.createStatement().executeQuery(
                                        "SELECT Актив.ActiveName, Пользователь.UserName, Операция.Amount, Операция.OperationType, Операция.IsSuccess, Операция.OperationDate " +
                                                "FROM Операция " +
                                                "JOIN Актив " +
                                                "ON Актив.ActiveID = Операция.ActiveID " +
                                                "JOIN Пользователь " +
                                                "ON Пользователь.UserID = Операция.UserID " +
                                                "ORDER BY Пользователь.UserName"
                                );
                            }
                        }
                        
                        while (resultSet.next()) {
                            Operation operation = new Operation(resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getInt(3),
                                    resultSet.getString(4),
                                    resultSet.getString(5),
                                    resultSet.getString(6)
                            );
                            saved.add(operation);
                        }
                    }
                    //endregion

                    default -> answer = "fail";
                }


                Log("Server output: " + ((isString)?answer:"@ArrayList<Object>"));
                Log("-------------------------------------------------");

                clientOutput.writeObject((isString)?answer:saved);

                clientSocket.close();
                clientInput.close();
                clientOutput.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //endregion


    //region Fields

    private ServerSocket serverSocket;
    private Socket acceptedClientSocket;
    private final FileWriter fileWriter;

    private final int port;
    private String logFileName = "logs.txt";

    //endregion


    //region Properties

    public ServerSocket getServerSocket() {
        return serverSocket;
    }


    public int getPort() {
        return port;
    }


    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    //endregion


    //region Class lifecycle

    public Server() throws IOException {
        this.serverSocket = null;
        this.acceptedClientSocket = null;
        this.port = 0;
        fileWriter = new FileWriter(logFileName, true);
    }


    public Server(int port) throws IOException {
        this.port = port;
        fileWriter = new FileWriter(logFileName, true);
    }

    //endregion


    //region Public methods

    public void startTCPServer() throws IOException {
        try {
            Connection connection = getConnection();
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            DatabaseMetaData dm = (DatabaseMetaData) connection.getMetaData();
            Log("-------------------------------------------------");
            Log("Driver name: " + dm.getDriverName());
            Log("Driver version: " + dm.getDriverVersion());
            Log("Product name: " + dm.getDatabaseProductName());
            Log("Product version: " + dm.getDatabaseProductVersion());
            Log("-------------------------------------------------");

            serverSocket = new ServerSocket(port);

            String serverLog = String.format("TCP Server starting at %s:%d",
                    serverSocket.getInetAddress(),
                    serverSocket.getLocalPort());

            System.out.print(serverLog);

            Log(serverLog);

            while(true) {
                acceptedClientSocket = serverSocket.accept();

                serverLog = String.format("Date: %s, Time: %s\n",
                        LocalDate.now(),
                        LocalTime.now());

                serverLog += String.format("New connection: %s : %d",
                        acceptedClientSocket.getLocalAddress(),
                        acceptedClientSocket.getPort());
                Log(serverLog);
                ClientHandler clientHandler = new ClientHandler(acceptedClientSocket, connection);
                new Thread(clientHandler).start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log(e.getMessage());
        }
        finally {
            Log("Shutting down...");
            if(serverSocket != null) {
                serverSocket.close();
            }
            if (acceptedClientSocket != null){
                acceptedClientSocket.close();
            }
            fileWriter.close();
        }
    }

    public static Connection getConnection() throws SQLException, IOException{

        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("database.properties"))){
            props.load(in);
        }
        String url = props.getProperty("url");
        String username = props.getProperty("username");
        String password = props.getProperty("password");

        return DriverManager.getConnection(url, username, password);
    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    //endregion


    //region Private methods

    private void Log(String stringToAppend) throws IOException {
        fileWriter.append(stringToAppend).append("\n");
        fileWriter.flush();
    }

    private byte [] convertDoubleToByteArray(double number) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Double.BYTES);
        byteBuffer.putDouble(number);
        return byteBuffer.array();
    }

    //endregion
}
