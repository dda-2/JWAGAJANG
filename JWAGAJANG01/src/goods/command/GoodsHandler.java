package goods.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.command.CommandHandler;
import goods.dao.GoodsDAO;
import goods.dto.GoodsVO;
import member.service.FavService;

public class GoodsHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();
		
		/* List<GoodsVO> mdDetail; */
		int md_code = Integer.parseInt(req.getParameter("md_code"));
		String userid = (String)session.getAttribute("id");
		
		// 상품
		GoodsDAO gDao = GoodsDAO.getInstance();
		GoodsVO md = gDao.getMd(md_code);
		
		FavService fs = FavService.getInstance();
		Integer dupchk = fs.checkFavDup(userid, md_code);
		if(dupchk != null) {
			req.setAttribute("dupchk", dupchk);
		}
		
		req.setAttribute("md", md);
		req.setAttribute("md_code", md_code);
		
		// 리뷰 수에 따른 리뷰 리스트 
		List<GoodsVO> reviewList;
	
		int count = gDao.reviewCount(md_code);
		
		if (count > 0) {
			reviewList = gDao.getReviewList(count, md_code);
			req.setAttribute("reviewList", reviewList);
		}
		
		req.setAttribute("count", new Integer(count));
		
		return "/goods.jsp";
		 
	}
}
