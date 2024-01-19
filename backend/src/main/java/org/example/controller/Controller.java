package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.entity.Device;
import org.example.entity.Message;
import org.example.entity.User;
import com.google.gson.Gson;
import org.example.mapper.DeviceMapper;
import org.example.mapper.MessageMapper;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@CrossOrigin(origins = {"*","null"})
@RestController
public class Controller {
    @Autowired
    UserMapper userMapper;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    MessageMapper messageMapper;
    private Gson gson = new Gson();

    @GetMapping("/test")
    public String test() {
        List<User> usersList = userMapper.selectList(null);
        return gson.toJson(usersList);
    }

    @PostMapping("/user/login")
    public String login(@RequestBody Map<String, String> loginData) {
        String name = loginData.get("name");
        String password = loginData.get("password");

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("name", name)
                .eq("password", password);

        User userSelected = userMapper.selectOne(userQueryWrapper);

        if (userSelected == null) {
            return "fail";
        } else {
            return "success";
        }
    }

    @PostMapping("/user/register")
    public String register(@RequestBody User user) {
        // 检查用户名和电子邮件是否唯一
        if (!isUsernameUnique(user.getName()) || !isEmailUnique(user.getEmail())) {
            return "The user name or email already exists";
        }

        // 验证密码长度
        if (user.getPassword().length() < 6) {
            return "The password must be 6 characters long or more";
        }

        // 验证电子邮件格式
        if (!isValidEmail(user.getEmail())) {
            return "Email format is invalid";
        }

        // 验证电话号码格式
        if (!isValidPhone(user.getPhone())) {
            return "The phone number format is invalid";
        }

        // 注册用户的逻辑...
        try {
            userMapper.insert(user);
        } catch (Exception e) {
            return "Registration failed, please try again later";
        }

        return "success";
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        // 此处假设电话号码只能包含数字并且长度为11位
        String phoneRegex = "^[0-9]{11}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phone).matches();
    }

    private boolean isUsernameUnique(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", username);
        int count = Math.toIntExact(userMapper.selectCount(queryWrapper));

        return count == 0;
    }

    private boolean isEmailUnique(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        int count = Math.toIntExact(userMapper.selectCount(queryWrapper));

        return count == 0;
    }

    @GetMapping("/user/getUserInfoByName")
    public ResponseEntity<User> getUserInfo(@RequestParam String username) {
        // Query the database or any other data source to retrieve the user information
        User user = userMapper.getUserByUsername(username);

        if (user != null) {
            // User found, return the user entity
            return ResponseEntity.ok(user);
        } else {
            // User not found, return an appropriate response
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/device/getAllDevices")
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceMapper.selectList(null);
        return ResponseEntity.ok(devices);
    }
    @PutMapping("/device/updateDevice")
    public ResponseEntity<String> updateDevice(@RequestBody Device device) {
        // 验证设备ID是否存在
        Device existingDevice = deviceMapper.selectById(device.getId());
        if (existingDevice == null) {
            return ResponseEntity.badRequest().body("Device with id " + device.getId() + " does not exist");
        }

        // 更新设备信息的逻辑...
        try {
            deviceMapper.updateById(device);
        } catch (Exception e) {
            System.out.println("Update failed with error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed, please try again later");
        }

        return ResponseEntity.ok("success");
    }
    @PutMapping("/user/updateUser")
    public ResponseEntity<String> updateDevice(@RequestBody User user) {
        // 验证设备ID是否存在
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            return ResponseEntity.badRequest().body("User with id " + user.getId() + " does not exist");
        }

        // 更新设备信息的逻辑...
        try {
            userMapper.updateById(user);
        } catch (Exception e) {
            System.out.println("Update failed with error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed, please try again later");
        }

        return ResponseEntity.ok("success");
    }
    @PostMapping("/device/createDevice")
    public ResponseEntity<String> addDevice(@RequestBody Device device) {
        // 插入新设备的逻辑...
        try {
            deviceMapper.insert(device);
        } catch (Exception e) {
            System.out.println("Insertion failed with error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Insertion failed, please try again later");
        }

        return ResponseEntity.ok("success");
    }
    @GetMapping("/message/getAllMessages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageMapper.selectList(null);
        return ResponseEntity.ok(messages);
    }
}
