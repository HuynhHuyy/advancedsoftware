package demo.cdcnpm.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Đây là session service
@Service
public class SessionService {

	@Autowired
    HttpSession session ; // khai báo session

    // setattribute
    public void set(String name,Object value){
        session.setAttribute(name, value);
    }
    public <T> T get(String name){
        return (T)session.getAttribute(name);
    }
    // xóa attribute theo tên
    public void remove(String name){
        session.removeAttribute(name);
    }
}
