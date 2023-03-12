///Wrong implementation

//unvalidated "clientName" parameter appended to the query allows an attacker to inject any SQL code

String query = "SELECT account_balance FROM user_data WHERE user_name = "
             + request.getParameter("clientName");
try {
    Statement statement = connection.createStatement( ... );
    ResultSet results = statement.executeQuery( query );
}


// Correct implementation

String clientname = request.getParameter("clientName");
// Validate input to detect attacks
String query = "SELECT account_balance FROM user_data WHERE user_name = ? ";
PreparedStatement pstmt = connection.prepareStatement( query );
pstmt.setString( 1, clientname);
ResultSet results = pstmt.executeQuery( );
