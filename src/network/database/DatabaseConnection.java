package network.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JOptionPane;

import network.RequestAndResponse.*;
import ui.LoginForm;
import ui.RegisterForm;

public class DatabaseConnection {
	private Connection connection;
    public DatabaseConnection() {
    	connectToDatabase();
    }
    private void connectToDatabase() {
    	try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://LAPTOP-MP2192TB:1433;databaseName=ChessGame;encrypt=true;trustServerCertificate=true";
            String userName = "sa";
            String password = "123456789";
            connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!");
        }
    }
    private UserResponse loginAuthentication(LoginRequest loginRequest) throws Exception {
        boolean isUserExits = false;
        boolean isPasswordCorrect = false;
        UserResponse user = new UserResponse();

        try {
            String query = "SELECT * FROM Users WHERE userName = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, loginRequest.getUserName());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    isUserExits = true;
                    String dbPassword = resultSet.getString("password");
                    isPasswordCorrect = dbPassword.equals(loginRequest.getPassword());

                    if (isPasswordCorrect) {
                        user.setUserId(resultSet.getInt("id"));
                        user.setUserName(resultSet.getString("username"));
                        user.setPassword(resultSet.getString("password"));
                        user.setelo(resultSet.getInt("elo"));
                        user.setWin(resultSet.getInt("win"));
                        user.setLose(resultSet.getInt("lose"));
                        user.setDraw(resultSet.getInt("draw"));
                        
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Lỗi khi truy vấn cơ sở dữ liệu: " + e.getMessage());
        }

        if (!isUserExits) {
            throw new Exception("User không tồn tại");
        }
        if (!isPasswordCorrect) {
            throw new Exception("Sai mật khẩu, vui lòng thử lại");
        }

        return user;
    }
    	private SimpleResponse registerNewUser(RegisterRequest registerRequest) throws Exception {
        boolean isUserNameExist = false;
        SimpleResponse response = new SimpleResponse();

        // Kiểm tra xem userName đã tồn tại trong cơ sở dữ liệu chưa
        String query = "SELECT * FROM Users WHERE userName = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, registerRequest.getUserName());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                isUserNameExist = true;
            }
        }

        // Đưa ra phản hồi dựa trên kết quả kiểm tra
        if (isUserNameExist) {
            response.msg = "User name already exist";
        } else {
            // Nếu không tồn tại, thêm người dùng mới vào cơ sở dữ liệu
        	String insertQuery = "INSERT INTO Users (username, password) VALUES (?, ?)";
        	try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
        	    insertStatement.setString(1, registerRequest.getUserName());
        	    insertStatement.setString(2, registerRequest.getPassword());
        	    insertStatement.executeUpdate();
        	    response.msg = "Create new account success, go back to login";
        	}

        }

        return response;
    }
    private RankingListResponse getRankingList(RankingListRequest rankingListRequest) throws Exception {
    	   RankingListResponse rankingListResponse = new RankingListResponse();
    	   String query = "SELECT username, elo FROM Rank ORDER BY elo DESC"; // Truy vấn lấy danh sách xếp hạng theo elo giảm dần

    	   try (PreparedStatement statement = connection.prepareStatement(query);
    	         ResultSet resultSet = statement.executeQuery()) {

    	        // Lấy từng bản ghi từ kết quả truy vấn và thêm vào danh sách xếp hạng
    		   while (resultSet.next()) {
    			   String userName = resultSet.getString("username");
    			   int elo = resultSet.getInt("elo");

    	            // Thêm vào danh sách (sử dụng Map, ví dụ: "username" -> "elo")
    			   rankingListResponse.addRanking(userName, elo);
    		   }
    	   } catch (Exception e) {
    	        	e.printStackTrace();
    	        	throw new Exception("Error fetching ranking list: " + e.getMessage());
    	   }
    	   
    	   return rankingListResponse;
    }


    private HistoryGame.Response getHistoryGame(HistoryGame.Request gameRequest) throws Exception {
        HistoryGame.Response response = new HistoryGame.Response();
        String query = "SELECT player_id, opponent_id, moves, result FROM HistoryGame WHERE macthid = ?"; // Truy vấn thông tin trận đấu

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, gameRequest.getGameId()); // Set macthid từ gameRequest vào truy vấn
            ResultSet resultSet = statement.executeQuery();

            // Kiểm tra nếu có dữ liệu trả về
            if (resultSet.next()) {
                // Lấy thông tin người chơi và đối thủ
                int playerId = resultSet.getInt("player_id");
                int opponentId = resultSet.getInt("opponent_id");
                String moves = resultSet.getString("moves");
                String result = resultSet.getString("result");

                // Thêm thông tin vào đối tượng response
                response.setPlayerId(playerId);
                response.setOpponentId(opponentId);
                response.setMoves(moves); // Lưu chuỗi nước đi PGN
                response.setResult(result); // Kết quả trận đấu
            } else {
                throw new Exception("Game not found with ID: " + gameRequest.getGameId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error fetching game history: " + e.getMessage());
        }

        return response;
    }
    private ProfileView.Response getProfile(ProfileView.Request request) throws Exception {
        ProfileView.Response response = new ProfileView.Response();
        String query = "SELECT id, username, email, elo, win, lose, draw FROM Users WHERE id = ?"; // Truy vấn thông tin người dùng theo userID

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, request.getUserID()); // Set userID từ request vào câu truy vấn
            ResultSet resultSet = statement.executeQuery();

            // Kiểm tra nếu có dữ liệu trả về
            if (resultSet.next()) {
                response.setUserId(resultSet.getInt("id"));
                response.setUserName(resultSet.getString("username"));
                response.setElo(resultSet.getInt("elo"));
                response.setWin(resultSet.getInt("win"));
                response.setLose(resultSet.getInt("lose"));
                response.setDraw(resultSet.getInt("draw"));
            } else {
                throw new Exception("User not found with ID: " + request.getUserID());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error fetching profile: " + e.getMessage());
        }

        return response;
    }

}
