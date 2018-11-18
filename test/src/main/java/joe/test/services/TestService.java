package joe.test.services;

import joe.test.model.query.TestQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: test
 * Date Date : 2018年09月28日 11:04
 */
@Service
public class TestService {

    private final TestQuery testQuery;

    @Autowired
    public TestService(TestQuery testQuery) {this.testQuery = testQuery;}

    public Object test() {
        return testQuery;
    }
}
