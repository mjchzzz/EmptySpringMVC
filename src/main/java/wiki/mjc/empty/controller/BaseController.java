package wiki.mjc.empty.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BaseController {

	@RequestMapping("/")
	public String HelloWorld() {
		return "helloworld";
	}
	
	@RequestMapping("/json")
	@ResponseBody
	public Map<String, Object> json() {
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("result", "hello world");
		return ret;
	}
}
