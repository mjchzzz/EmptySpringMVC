package wiki.mjc.empty.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import wiki.mjc.empty.utils.ElasticSearchUtil;

@Controller
public class BaseController {

	protected Logger logger = LogManager.getLogger("test");
	
	@Autowired
	ElasticSearchUtil elasticSearchUtil;
			
	@RequestMapping("/")
	public String HelloWorld() {
		logger.info("helloworld");
		//elasticSearchUtil.init();
		
		return "helloworld";
	}
	
	@RequestMapping("/put")
	@ResponseBody
	public Map<String, Object> json(@RequestParam String value) {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("result", "hello world");
		elasticSearchUtil.ttt(value);
		return ret;
	}
}
