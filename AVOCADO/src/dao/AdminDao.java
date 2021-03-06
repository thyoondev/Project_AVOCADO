package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import common.DBOracleConnection;
import common.commonJDBC;
import dto.FreeboardDto;
import dto.KickboardDto;
import dto.MemberDto;
import dto.RentDto;

public class AdminDao {

  DBOracleConnection common = new DBOracleConnection();
  Connection connection = null;
  PreparedStatement ps = null;
  ResultSet rs = null;
  commonJDBC jdbc = new commonJDBC();

  /**
   * 렌트 테이블 쿼리 조회
   */
  public ArrayList<RentDto> getUsingServiceUser() {
    ArrayList<RentDto> arr = new ArrayList<RentDto>();

    String query = "select * from ta_rent where rent_stats = 0";

    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        String no = rs.getString(1);
        String member_no = rs.getString(2);
        String kickboard_no = rs.getString(3);
        String startdate = rs.getString(4);
        String enddate = rs.getString(5);
        int useddate = rs.getInt(6);
        int stats = rs.getInt(7);
        int cost = rs.getInt(8);
        RentDto dto = new RentDto(no, member_no, kickboard_no, startdate, enddate, useddate, stats, cost);
        arr.add(dto);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }

    return arr;
  }

  /**
   * 멤버 번호로 멤버 검색 검색 값 없으면 전체조회
   * 
   * @param member_no 멤버 번호
   * @return 멤버 쿼리
   */
  public ArrayList<MemberDto> getUser(String member_no) {
    ArrayList<MemberDto> arr = new ArrayList<MemberDto>();

    String query = "select *\r\n" + "from ta_member\r\n" + "where member_no like '%" + member_no + "%' order by member_no asc";

    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        String no = rs.getString("member_no");
        String name = rs.getString("member_name");
        String email = rs.getString("member_email");
        String phoneNumber = rs.getString("member_phoneNumber");
        String password = "";
        String lastRentDate = rs.getString("member_lastRentDate");
        int money = rs.getInt("member_money");
        int useTimes = rs.getInt("member_useTimes");
        int accept = rs.getInt("member_accept");
        int useCount = rs.getInt("member_useCount");
        String rank = rs.getString("member_rank");
        String regDate = rs.getString("member_regDate");
        MemberDto dto = new MemberDto(no, name, email, phoneNumber, password, lastRentDate, money, useTimes, rank, regDate, useCount, accept);
        arr.add(dto);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }

    return arr;
  }


  /**
   * 킥보드 번호로 킥보드 검색 검색 값 없으면 전체조회
   * 
   * @param kickboard_no 킥보드 번호
   * @return 킥보드 쿼리
   */
  public ArrayList<KickboardDto> getKickboard(String kickboard_no) {
    ArrayList<KickboardDto> arr = new ArrayList<KickboardDto>();
    String query = "select *\r\n" + "from ta_kickboard\r\n" + "where kickboard_no like '%" + kickboard_no + "%' order by kickboard_no asc";

    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        String no = rs.getString("kickboard_no");
        String kickboard_name = rs.getString("kickboard_name");
        String kickboard_type = rs.getString("kickboard_type");
        String kickboard_location = rs.getString("kickboard_location");
        String kickboard_regdate = rs.getString("kickboard_regdate");
        String kickboard_img = "";
        int kickboard_rentstats = rs.getInt("kickboard_rentstats");
        int kickboard_totalusedtimes = rs.getInt("kickboard_totalusedtimes");
        int kickboard_battery = rs.getInt("kickboard_battery");
        KickboardDto dto = new KickboardDto(no, kickboard_name, kickboard_type, kickboard_location, kickboard_regdate, kickboard_img, kickboard_rentstats, kickboard_totalusedtimes, kickboard_battery);
        arr.add(dto);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }

    return arr;
  }

  /**
   * 멤버번호나 킥보드 번호로 서비스 이용 내역 검색
   * 
   * @param no 멤버 번호나 킥보드 번호
   * @param serchType 구분값
   * @return 렌트 테이블 쿼리
   */
  public ArrayList<RentDto> getRentHistory(String no, String serchType) {
    ArrayList<RentDto> arr = new ArrayList<RentDto>();
    String query = "";
    if (serchType.equals("member")) {
      query = "select rent.rent_no,rent.rent_member_no,member.member_name,rent.rent_kickboard_no,rent.rent_startdate,rent.rent_enddate,rent.rent_useddate,rent.rent_stats,rent.rent_cost\r\n" + "from ta_rent rent, ta_member member\r\n"
          + "where rent.rent_member_no = member.member_no and rent.rent_member_no = '" + no + "' order by rent.rent_no desc";
    } else if (serchType.equals("kickboard")) {
      query = "select rent.rent_no,rent.rent_member_no,member.member_name,rent.rent_kickboard_no,rent.rent_startdate,rent.rent_enddate,rent.rent_useddate,rent.rent_stats,rent.rent_cost\r\n" + "from ta_rent rent, ta_member member\r\n" + "where rent.rent_member_no = member.member_no\r\n"
          + "and rent.rent_kickboard_no = '" + no + "'\r\n" + "order by rent.rent_no desc";
    } else if (serchType.equals("")) {
      query = "select rent.rent_no,rent.rent_member_no,member.member_name,rent.rent_kickboard_no,rent.rent_startdate,rent.rent_enddate,rent.rent_useddate,rent.rent_stats,rent.rent_cost\r\n" + "from ta_rent rent, ta_member member\r\n" + "where rent.rent_member_no = member.member_no\r\n"
          + "and rent.rent_member_no like '%" + no + "%'\r\n" + "order by rent.rent_no desc";
    }

    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        String rent_no = rs.getString("rent_no");
        String rent_member_no = rs.getString("rent_member_no");
        String member_name = rs.getString("member_name");
        String rent_kickboard_no = rs.getString("rent_kickboard_no");
        String rent_startdate = rs.getString("rent_startdate");
        String rent_enddate = rs.getString("rent_enddate");
        int rent_useddate = rs.getInt("rent_useddate");
        int rent_stats = rs.getInt("rent_stats");
        int rent_cost = rs.getInt("rent_cost");
        RentDto dto = new RentDto(rent_no, rent_member_no, member_name, rent_kickboard_no, rent_startdate, rent_enddate, rent_useddate, rent_stats, rent_cost);
        arr.add(dto);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }

    return arr;
  }

  /**
   * 
   * @param sessionEmail 현재 로그인 중인 세션 멤버 이메일 주소
   * @return 해당 멤버의 번호
   */
  public String getMemberNo(String sessionEmail) {
    String member_no = "";
    String query = "select member_no\r\n" + "from ta_member\r\n" + "where member_email = '" + sessionEmail + "'";

    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      if (rs.next()) {
        member_no = rs.getString(1);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }
    return member_no;
  }

  // 오늘 킥보드 이용시간
  public int getTodayUsedTime(String no) {
    int time = 0;
    String query = "SELECT sum(rent_useddate)\r\n" + "FROM ta_rent\r\n" + "where rent_member_no = '" + no + "'\r\n" + "and TO_CHAR(rent_enddate, 'YYYYMMDD')  = TO_CHAR(SYSDATE, 'YYYYMMDD')";
    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      if (rs.next()) {
        time = rs.getInt(1);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }
    return time;
  }

  // 어제 킥보드 이용시간
  public int getYesterdayUsedTime(String no) {
    int time = 0;
    String query = "SELECT sum(rent_useddate)\r\n" + "FROM ta_rent\r\n" + "where rent_member_no = '" + no + "'\r\n" + "and TO_CHAR(rent_enddate, 'YYYYMMDD')  = TO_CHAR(SYSDATE-1, 'YYYYMMDD')";
    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      if (rs.next()) {
        time = rs.getInt(1);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }
    return time;
  }

  // 2일전 킥보드 이용시간
  public int get2daysagoUsedTime(String no) {
    int time = 0;
    String query = "SELECT sum(rent_useddate)\r\n" + "FROM ta_rent\r\n" + "where rent_member_no = '" + no + "'\r\n" + "and TO_CHAR(rent_enddate, 'YYYYMMDD')  = TO_CHAR(SYSDATE-2, 'YYYYMMDD')";
    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      if (rs.next()) {
        time = rs.getInt(1);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }
    return time;
  }

  // 3일전 킥보드 이용시간
  public int get3daysagoUsedTime(String no) {
    int time = 0;
    String query = "SELECT sum(rent_useddate)\r\n" + "FROM ta_rent\r\n" + "where rent_member_no = '" + no + "'\r\n" + "and TO_CHAR(rent_enddate, 'YYYYMMDD')  = TO_CHAR(SYSDATE-3, 'YYYYMMDD')";
    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      if (rs.next()) {
        time = rs.getInt(1);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }
    return time;
  }

  // 4일전 킥보드 이용시간
  public int get4daysagoUsedTime(String no) {
    int time = 0;
    String query = "SELECT sum(rent_useddate)\r\n" + "FROM ta_rent\r\n" + "where rent_member_no = '" + no + "'\r\n" + "and TO_CHAR(rent_enddate, 'YYYYMMDD')  = TO_CHAR(SYSDATE-4, 'YYYYMMDD')";
    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      if (rs.next()) {
        time = rs.getInt(1);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }
    return time;
  }

  // 5일전 킥보드 이용시간
  public int get5daysagoUsedTime(String no) {
    int time = 0;
    String query = "SELECT sum(rent_useddate)\r\n" + "FROM ta_rent\r\n" + "where rent_member_no = '" + no + "'\r\n" + "and TO_CHAR(rent_enddate, 'YYYYMMDD')  = TO_CHAR(SYSDATE-5, 'YYYYMMDD')";
    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      if (rs.next()) {
        time = rs.getInt(1);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }
    return time;
  }

  // 6일전 킥보드 이용시간
  public int get6daysagoUsedTime(String no) {
    int time = 0;
    String query = "SELECT sum(rent_useddate)\r\n" + "FROM ta_rent\r\n" + "where rent_member_no = '" + no + "'\r\n" + "and TO_CHAR(rent_enddate, 'YYYYMMDD')  = TO_CHAR(SYSDATE-6, 'YYYYMMDD')";
    try {
      connection = common.getConnection();
      ps = connection.prepareStatement(query);
      rs = ps.executeQuery();
      if (rs.next()) {
        time = rs.getInt(1);
      }
    } catch (SQLException se) {
      jdbc.printSQLExceptionError(query);
    } catch (Exception ee) {
      jdbc.printExceptionError();
    } finally {
      common.close(connection, ps, rs);
    }
    return time;
  }

}
