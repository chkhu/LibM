import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;//导入java.sql包中的所有类
import java.util.Scanner;//导入java.util包中的Scanner类,用于获取用户输入

public class LibManager {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";//JDBC驱动名
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library"; //数据库连接地址

    //数据库用户名和密码
    private static final String USER = "root";
    private static final String PASSWORD = "030531";

    //数据库连接对象，用来连接database
    private static Connection conn = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;

    public static void main(String[] args) {

        // 连接数据库
        try {
            Class.forName(JDBC_DRIVER);
            //连接数据库
            conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException e) {//捕获ClassNotFoundException和SQLException异常
            e.printStackTrace();
        }

        String[] tableNames = { "book", "card", "borrowRecord" };

        // // debug
        // // 检查是否已经存在book、card和borrowRecord三张表，如果存在则删除
        // for (String tableName : tableNames) {
        //     String sql = "DROP TABLE IF EXISTS " + tableName;
        //     try {
        //         stmt.executeUpdate(sql);
        //     } catch (SQLException e) {
        //         e.printStackTrace();
        //     }
        // }

        //如果数据库不存在book、card和borrowRecord三张表, 则创建它们并初始化
        for (String tableName : tableNames) {
            String sql = "SELECT * FROM " + tableName;
            try {
                rs = stmt.executeQuery(sql);
            } catch (SQLException e) {
                // e.printStackTrace();
                // 如果查询失败，则说明表不存在，需要创建
                createTable();
            }
        }

        //如果数据库已经存在book、card和borrowRecord三张表，则不做任何操作

        // 提供基本操作菜单
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            System.out.println("请选择操作：");
            System.out.println("1. 新增馆藏书籍");
            System.out.println("2. 删除书籍");
            System.out.println("3. 根据书名检索书籍");
            System.out.println("4. 借阅书籍");
            System.out.println("5. 归还书籍");
            System.out.println("6. 新增借书卡");
            System.out.println("7. 注销借书卡");
            System.out.println("8. 显示所有馆藏书籍");
            System.out.println("9. 显示所有借阅卡信息");
            System.out.println("0. 退出程序");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    deleteBook();
                    break;
                case 3:
                    searchBook();
                    break;
                case 4:
                    borrowBook();
                    break;
                case 5:
                    returnBook();
                    break;
                case 6:
                    addCard();
                    break;
                case 7:
                    deleteCard();
                    break;
                case 8:
                    showAllBooks();
                    break;
                case 9:
                    showAllCards();
                    break;
                case 0:
                    System.out.println("程序已退出");
                    System.exit(0);
                    break;
                default:
                    System.out.println("无效的选择，请重新选择");
                    break;
            }
        }

        // 关闭数据库连接
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 随机生成书籍的标题
    private static String getRandomTitle() {
        String[] titles = { "Java从入门到精通", "Python编程基础", "数据结构与算法分析", "计算机网络原理", "人工智能导论",
                "深入浅出MySQL", "Web开发实战", "JavaScript高级程序设计", "C++程序设计基础", "算法竞赛入门经典" };
        int index = (int) (Math.random() * titles.length);
        return titles[index];
    }

    // 随机生成书籍的作者
    private static String getRandomAuthor() {
        String[] authors = { "张三", "李四", "王五", "赵六", "钱七" };
        int index = (int) (Math.random() * authors.length);
        return authors[index];
    }

    // 随机生成书籍的出版社
    private static String getRandomPress() {
        String[] presses = { "机械工业出版社", "电子工业出版社", "人民邮电出版社", "清华大学出版社", "高等教育出版社" };
        int index = (int) (Math.random() * presses.length);
        return presses[index];
    }

    // 随机生成借书人
    private static String getRandomName() {
        String[] names = { "张三", "李四", "王五", "赵六", "钱七" };
        int index = (int) (Math.random() * names.length);
        return names[index];
    }

    //下面是跟sql语句相关的 各个功能的实现

    // 新增馆藏书籍
    // private static void addBook() {
    //     Scanner scanner = new Scanner(System.in);
    //     System.out.println("请输入书名：");
    //     String title = scanner.nextLine();
    //     System.out.println("请输入作者：");
    //     String author = scanner.nextLine();
    //     System.out.println("请输入出版社：");
    //     String press = scanner.nextLine();
    //     String sql = "INSERT INTO book (title, author, press, status) VALUES (" +
    //             "'" + title + "'," +
    //             "'" + author + "'," +
    //             "'" + press + "'," +
    //             "0" +
    //             ")";
    //     try {
    //         stmt.executeUpdate(sql);
    //         System.out.println("新增书籍成功");
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
    private static void addBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请选择插入方式：");
        System.out.println("1. 单本插入");
        System.out.println("2. 批量插入");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                System.out.println("请输入书名：");
                String title = scanner.nextLine();
                System.out.println("请输入作者：");
                String author = scanner.nextLine();
                System.out.println("请输入出版社：");
                String press = scanner.nextLine();
                single_insertBook(title, author, press);
                break;
            case 2:
                System.out.println("请输入文件名（包含扩展名）：");
                String filename = scanner.nextLine();
                batchInsertBooks(filename);
                break;
            default:
                System.out.println("无效的选择");
                break;
        }
    }

    // 单本插入书籍
    private static void single_insertBook(String title, String author, String press) {
        String sql = "INSERT INTO book (title, author, press, status) VALUES (" +
                "'" + title + "'," +
                "'" + author + "'," +
                "'" + press + "'," +
                "0" +
                ")";
        try {
            stmt.executeUpdate(sql);
            System.out.println("新增书籍成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 批量插入书籍
    private static void batchInsertBooks(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] bookInfo = line.split(",");
                if (bookInfo.length >= 3) {
                    String title = bookInfo[0].trim(); //不同项目之间用trim()去除空格
                    String author = bookInfo[1].trim();
                    String press = bookInfo[2].trim();
                    single_insertBook(title, author, press);
                } else {
                    System.out.println("无效的书籍信息：" + line);
                }
            }
            System.out.println("批量插入完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 删除书籍
    private static void deleteBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入要删除的书籍ID：");
        int bookId = scanner.nextInt();
        String sql = "DELETE FROM book WHERE book_id=" + bookId;
        try {
            stmt.executeUpdate(sql);
            System.out.println("删除书籍成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 根据书名检索书籍
    private static void searchBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入书名关键词：");
        String keyword = scanner.nextLine();
        String sql = "SELECT * FROM book WHERE title LIKE '%" + keyword + "%'";
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String press = rs.getString("press");
                int status = rs.getInt("status");
                String statusStr = status == 0 ? "可借" : "已借出";
                System.out.println(
                        "书籍ID：" + bookId + "，书名：" + title + "，作者：" + author + "，出版社：" + press + "，状态：" + statusStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 借阅书籍
    private static void borrowBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入借书证号：");
        int cardId = scanner.nextInt();
        System.out.println("请输入书籍ID：");
        int bookId = scanner.nextInt();

        // 检查借书证和书籍是否存在
        boolean cardExists = false, bookExists = false;
        String sql = "SELECT * FROM card WHERE card_id=" + cardId;
        try {
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                cardExists = true;
            } else {
                System.out.println("借书证不存在");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT * FROM book WHERE book_id=" + bookId;
        try {
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int status = rs.getInt("status");
                if (status == 0) {
                    bookExists = true;
                } else {
                    System.out.println("书籍已借出");
                    return;
                }
            } else {
                System.out.println("书籍不存在");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 借阅书籍
        if (cardExists && bookExists) {
            sql = "UPDATE book SET status=1 WHERE book_id=" + bookId;
            try {
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            java.sql.Date borrowDate = new java.sql.Date(System.currentTimeMillis());
            java.sql.Date returnDate = new java.sql.Date(System.currentTimeMillis() + 30 * 24 * 3600 * 1000L); // 借阅期限为30天
            sql = "INSERT INTO borrowRecord (card_id, book_id, borrow_date, return_date) VALUES (" +
                    cardId + "," +
                    bookId + "," +
                    "'" + borrowDate.toString() + "'," +
                    "'" + returnDate.toString() + "'" +
                    ")";
            try {
                stmt.executeUpdate(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("借阅成功，借阅日期：" + borrowDate.toString() + "，归还日期：" + returnDate.toString());
        }
    }

    // 归还书籍
    private static void returnBook() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入借书证号：");
        int cardId = scanner.nextInt();
        System.out.println("请输入书籍ID：");
        int bookId = scanner.nextInt();

        // 检查借书证和书籍是否存在
        boolean cardExists = false, bookExists = false, borrowed = false;
        String sql = "SELECT * FROM card WHERE card_id=" + cardId;
        try {
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                cardExists = true;
            } else {
                System.out.println("借书证不存在");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT * FROM book WHERE book_id=" + bookId;
        try {
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int status = rs.getInt("status");
                if (status == 1) {
                    bookExists = true;
                } else {
                    System.out.println("书籍未借出");
                    return;
                }
            } else {
                System.out.println("书籍不存在");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 归还书籍
        if (cardExists && bookExists) {
            sql = "SELECT * FROM borrowRecord WHERE card_id=" + cardId + " AND book_id=" + bookId;
            try {
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    borrowed = true;
                    int recordId = rs.getInt("record_id");
                    java.sql.Date borrowDate = rs.getDate("borrow_date");
                    java.sql.Date returnDate = rs.getDate("return_date");
                    java.sql.Date actualReturnDate = new java.sql.Date(System.currentTimeMillis());
                    long days = (actualReturnDate.getTime() - returnDate.getTime()) / (24 * 3600 * 1000L);
                    if (days > 0) {
                        System.out.println("超期" + days + "天，需要缴纳罚款：" + days * 0.1 + "元");
                    }
                    sql = "DELETE FROM borrowRecord WHERE record_id=" + recordId;
                    try {
                        stmt.executeUpdate(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    sql = "UPDATE book SET status=0 WHERE book_id=" + bookId;
                    try {
                        stmt.executeUpdate(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("归还成功，借阅日期：" + borrowDate.toString() + "，应还日期：" + returnDate.toString()
                            + "，实际还书日期：" + actualReturnDate.toString());
                } else {
                    System.out.println("借阅记录不存在");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // 新增借书卡
    private static void addCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入借书人姓名：");
        String name = scanner.nextLine();
        String sql = "INSERT INTO card (name) VALUES (" +
                "'" + name + "'" +
                ")";
        try {
            stmt.executeUpdate(sql);
            System.out.println("新增借书卡成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 注销借书卡
    private static void deleteCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入借书卡ID：");
        int cardId = scanner.nextInt();
        String sql = "DELETE FROM card WHERE card_id=" + cardId;
        try {
            stmt.executeUpdate(sql);
            System.out.println("注销借书卡成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 显示所有馆藏书籍
    private static void showAllBooks() {
        String sql = "SELECT * FROM book";
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String press = rs.getString("press");
                int status = rs.getInt("status");
                String statusStr = status == 0 ? "可借" : "已借出";
                System.out.println(
                        "书籍ID：" + bookId + "，书名：" + title + "，作者：" + author + "，出版社：" + press + "，状态：" + statusStr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 显示所有借阅卡信息
    private static void showAllCards() {
        String sql = "SELECT * FROM card";
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int cardId = rs.getInt("card_id");
                String name = rs.getString("name");
                System.out.println("借阅卡ID：" + cardId + "，姓名：" + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTable() {
        // 创建book、card和borrowRecord三张表
        try {
            String sql = "CREATE TABLE book (" +
                    "book_id INT NOT NULL AUTO_INCREMENT," +
                    "title VARCHAR(50)," +
                    "author VARCHAR(50)," +
                    "press VARCHAR(50)," +
                    "status INT," +
                    "PRIMARY KEY (book_id)" +
                    ")";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE card (" +
                    "card_id INT NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(50)," +
                    "PRIMARY KEY (card_id)" +
                    ")";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE borrowRecord (" +
                    "record_id INT NOT NULL AUTO_INCREMENT," +
                    "card_id INT," +
                    "book_id INT," +
                    "borrow_date DATE," +
                    "return_date DATE," +
                    "PRIMARY KEY (record_id)" +
                    ")";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 初始化，向book表中插入20本随机书籍的记录
        try {
            for (int i = 0; i < 20; i++) {
                String sql = "INSERT INTO book (title, author, press, status) VALUES (" +
                        "'" + getRandomTitle() + "'," +
                        "'" + getRandomAuthor() + "'," +
                        "'" + getRandomPress() + "'," +
                        "0" +
                        ")";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 初始化，向card表中插入5张随机借书证的记录，对应五个初始的借阅人
        try {
            for (int i = 0; i < 5; i++) {
                String sql = "INSERT INTO card (name) VALUES (" +
                        "'" + getRandomName() + "'" +
                        ")";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}