package com.atlas.crawler.core;

import java.sql.Connection;


public class SQL {


    public Connection connection(){


        try {
//            Session currentSession = entityManager.unwrap(Session.class);

//            currentSession.doReturningWork(new ReturningWork<ResultSet>()
//            {
//
//                @Override
//                public ResultSet execute(Connection connection) throws SQLException {
//                    PreparedStatement stmt =
//                            connection.prepareStatement("SELECT count(p.id) FROM product p");
//                    ResultSet rs = stmt.executeQuery();
//                    rs.next();
//                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n");
//                    System.out.println(rs.getInt(0));
//                    System.out.println("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//                    System.exit(0);
//                    return null;
//                }
//            });



//            return DriverManager.getConnection(sqlUrl, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
