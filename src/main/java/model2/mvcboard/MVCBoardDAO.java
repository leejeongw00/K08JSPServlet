package model2.mvcboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.DBConnPool;

//모델2 방식의 자료실형 게시판에서 사용할 DAO 클래스 생성
//DB연결은 커넥션풀(DBCP)을 사용한다.
public class MVCBoardDAO extends DBConnPool{
   
   //부모클래스의 기본생성자 호출을 통해 DB를 연결한다.
    public MVCBoardDAO() {
        super();
    }
    
    //mvcboard 테이블의 게시물의 갯수를 카운트하여 반환한다.
    //목록의 페이징 처리나 게시물의 가상번호 부여에 사용한다.
    public int selectCount(Map<String, Object> map) {
        int totalCount = 0;
        String query = "SELECT COUNT(*) FROM mvcboard ";
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " "
                   + " LIKE '%" + map.get("searchWord") + "%'";
        }
        
        try {
            stmt = con.createStatement();    
            rs = stmt.executeQuery(query);   
            rs.next();   
            totalCount = rs.getInt(1);  
        }
        catch (Exception e) {
            System.out.println("게시물 카운트 중 예외 발생");
            e.printStackTrace();
        }
        return totalCount; 
    }
    
    /*
    모델1 방식에서는 board 테이블 및 boardDTO 클래스를 사용했지만
    모델2 방식에서는 mvcboard 테이블 및 MVCBoardDTO 클래스를 사용하므로
    해당 코드만 수정하면 된다.
    
    모델2 방식의 게시판 목록에 대한 페이징 처리 쿼리문 실행
     */
    public List<MVCBoardDTO> selectListPage(Map<String, Object> map) { 
        List<MVCBoardDTO> board = new ArrayList<MVCBoardDTO>();  
        String query = " "
              + " SELECT * FROM ( "
             + "    SELECT Tb.*, ROWNUM rNum FROM ( "
                + "        SELECT * FROM mvcboard ";
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " "
                   + " LIKE '%" + map.get("searchWord") + "%' ";
        }
        query += "      ORDER BY idx DESC "
                + "     ) Tb "
                + " ) "
                + " WHERE rNum BETWEEN ? AND ?";
        try {           
           psmt = con.prepareStatement(query);
            psmt.setString(1, map.get("start").toString());
            psmt.setString(2, map.get("end").toString());
            rs = psmt.executeQuery();
            
            while (rs.next()) {
                MVCBoardDTO dto = new MVCBoardDTO(); 
                dto.setIdx(rs.getString(1));   
                dto.setName(rs.getString(2));  
                dto.setTitle(rs.getString(3));      
                dto.setContent(rs.getString(4));   
                dto.setPostdate(rs.getDate(5));  
                dto.setOfile(rs.getString(6));
                dto.setSfile(rs.getString(7));  
                dto.setDowncount(rs.getInt(8));  
                dto.setPass(rs.getString(9));             
                dto.setVisitcount(rs.getInt(10));  
                
                board.add(dto);  
            }
        } 
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
        return board;
    }
    //글쓰기 처리시 첨부파일까지 함께 입력
    public int insertWrite(MVCBoardDTO dto) {
       int result = 0;
       try {
          /*
          컬럼 설명
          ofile : 원본파일명
          sfile : 서버에 저장된 파일명
          pass : 비회원제 게시판이므로 수정, 삭제를 위한 인증에 사용됨
           */
            String query = "INSERT INTO mvcboard ( "
                         + " idx, name, title, content, ofile, sfile, pass) "
                         + " VALUES ( "
                         + " seq_board_num.NEXTVAL,?,?,?,?,?,?)";  
            psmt = con.prepareStatement(query);   
            psmt.setString(1, dto.getName());  
            psmt.setString(2, dto.getTitle());
            psmt.setString(3, dto.getContent());
            psmt.setString(4, dto.getOfile());
            psmt.setString(5, dto.getSfile());
            psmt.setString(6, dto.getPass());
            result = psmt.executeUpdate(); 
        }
        catch (Exception e) {
            System.out.println("게시물 입력 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }
    
    //매개변수로 전달된 일련번호(idx)를 통해 하나의 레코드 조회
    public MVCBoardDTO selectView(String idx) {
       
       //조회된 레코드를 DTO객체 저장한 후 반환한다.
       MVCBoardDTO dto = new MVCBoardDTO();
       //인파라미터가 있는 쿼리문 작성
       String query = "SELECT * FROM mvcboard WHERE idx=?";
       try {
          //쿼리 실행을 위한 객체생성 및 인파라미터 설정
          psmt = con.prepareStatement(query);
          psmt.setString(1, idx);
          rs = psmt.executeQuery();
          
          //조회된 레코드가 있을 때 DTO 객체에 각 컬럼값을 저장한다.
          if (rs.next()) {
             dto.setIdx(rs.getString(1));
             dto.setName(rs.getString(2));
             dto.setTitle(rs.getString(3));
             dto.setContent(rs.getString(4));
             dto.setPostdate(rs.getDate(5));
             dto.setOfile(rs.getString(6));
             dto.setSfile(rs.getString(7));
             dto.setDowncount(rs.getInt(8));
             dto.setPass(rs.getString(9));
             dto.setVisitcount(rs.getInt(10));
          }
       }
       catch (Exception e) {
          System.out.println("게시물 상세보기 중 예외 발생");
          e.printStackTrace();
       }
       return dto;
    }
    //주어진 일련번호에 해당하는 게시물의 조회수를 1 증가시킨다.
    public void updateVisitCount(String idx) {
       String query ="UPDATE mvcboard SET "
             + " visitcount=visitcount+1 "
             + " WHERE idx=?";
       try {
          psmt = con.prepareStatement(query);
          psmt.setString(1, idx);
          psmt.executeQuery();
       }
       catch(Exception e) {
          System.out.println("게시물 조회수 증가 중 예외 발생");
          e.printStackTrace();
       }
    }
    //다운로드 수 증가
    public void downCountPlus(String idx) {
       String sql = "UPDATE mvcboard SET "
             + " downcount=downcount+1 "
             + " WHERE idx=? ";
       try {
          psmt = con.prepareStatement(sql);
          psmt.setString(1, idx);
          psmt.executeUpdate();
       }
       catch(Exception e) {}
    }
    
    
  //수정/삭제전 해당 게시물의 패스워드를 검증한다.
  	//즉, 게시물의 일련번호와 패스워드를 통해 조건에 부합하는 레코드가 있는지 확인한다.
  	public boolean confirmPassword(String pass, String idx) {
  		boolean isCorr = true;
  		try {
  			String sql = "SELECT COUNT(*) FROM mvcboard WHERE pass=? AND idx=?";
  			psmt = con.prepareStatement(sql);
  			psmt.setString(1, pass);
  			psmt.setString(2, idx);
  			rs = psmt.executeQuery();
  			
  			/*
  			select문에 count()함수를 사용하는 경우 조건(where 절)에 만족하는 레코드가
  			없을 때는 0, 만족하는 레코드가 있을떄는 1이상의 정수값이 반환된다.
  			즉 어떤 경우에도 결과값이 있으므로 next() 호출시 if문이 필요하지 않다.
  			 */
  			rs.next();
  			
  			/*
  			if(rs.next())
  				=> 일반적인 select문을 사용하는 경우에 1개의 레코드만 반환될때 사용한다.
  				주로 일련번호를 조건으로 추가하는 경우인데, 만족하는 레코드가 없다면
  				next()를 사용할 때 에러가 발생된다.
  			while(rs.next())
  				=>select의 결과가 2개 이상일때 사용된다. 주로 게시판의 목록과 같이
  				여러개의 레코드가 반환되는 경우에 사용한다.
  			 */
  			
  			//일치하는 결과가 없는 경우 false를 반환한다.
  			if(rs.getInt(1) == 0) {
  				isCorr = false;
  			}
  			
  		}
  		catch(Exception e) {
  			//예외가 발생하면 확인이 안된것이므로 이때도 false를 반환한다.
  			isCorr = false;
  			e.printStackTrace();
  		}
  		//검증이 완료되었을때만 true를 반환한다.
  		return isCorr;
  	}
  	
  	//게시물 삭제하기
  	public int deletePost(String idx) {
  		int result = 0;
  		try {
  			String query = "DELETE FROM mvcboard WHERE idx=?";
  			psmt = con.prepareStatement(query);
  			psmt.setString(1, idx);
  			result= psmt.executeUpdate();
  		}
  		catch(Exception e) {
  			System.out.println("게시물 삭제 중 예외 발생");
  		}
  		return result;
  	}
  	
  	public int updatePost(MVCBoardDTO dto) {
  		int result = 0;
  		
  		try {
  			//update쿼리문 작성
  			String query = "UPDATE mvcboard "
  						+ " SET title=?, name=?, content=?, ofile=?, sfile=? "
  						+ " WHERE idx=? and pass=?";
  			//동적쿼리문 실행을 위해 prepared객체 생성 및 인파라미터 설정
  			psmt = con.prepareStatement(query);
  			psmt.setString(1, dto.getTitle());
  			psmt.setString(2, dto.getName());
  			psmt.setString(3, dto.getContent());
  			psmt.setString(4, dto.getOfile());
  			psmt.setString(5, dto.getSfile());
  			psmt.setString(6, dto.getIdx());
  			psmt.setString(7, dto.getPass());
  			
  			//쿼리 실행 및 결과 반환(update된 행의 갯수)
  			result = psmt.executeUpdate();
  		}
  		catch(Exception e) {
  			System.out.println("게시물 수정 중 예외 발생");
  			e.printStackTrace();
  		}
  		
  		
  		return result;
  	}
}
