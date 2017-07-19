package chengzuo.Bean;
import java.util.List;

/***
 * 
 * @author tiffy
 */
public class TestCase {

	//ID
	String  testCaseID;
	//list of process
	List<myProcess> processList;
	//state of testcase
	String  state;
	//result of testcase
	TestCaseResult  result;
	//detail of testcase, String of socket received
	String  detail;

	public TestCase() {
	}

	public TestCase(String testCaseID, String state, TestCaseResult result, String detail) {
		this.testCaseID = testCaseID;
		this.state = state;
		this.result = result;
		this.detail = detail;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getTestCaseID() {
		return testCaseID;
	}

	public void setTestCaseID(String testCaseID) {
		this.testCaseID = testCaseID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}



	public TestCaseResult getResult() {
		return result;
	}

	public void setResult(TestCaseResult result) {
		this.result = result;
	}

	public List<myProcess> getProcessList() {
		return processList;
	}

	public void setProcessList(List<myProcess> list) {
		this.processList = list;
	}

	@Override
	public String toString() {
		String tmp = "TestCase [testCaseID=" + testCaseID + ", processList=\n";
		for (myProcess m : processList) {
			tmp = tmp + "\tmyProcess [processID=" + m.processID + ", processName=" + m.processName + ", processParam=" + m.processParam
					+ ", processStatus=" + m.processStatus + ", processExec=" + m.processExec + "]\n";
		}
		tmp = tmp + ", state=" + state + ", result="+ result + ", detail=" + detail + "]";
		return tmp;
	}
	/***
	 *  show format
	 * @return
	 */
	public String showTestCase(){
		String tmp="测试用例ID:" + testCaseID + "\n  -->激励链表: [ ";
		for (myProcess m : processList) {
			tmp = tmp + "\n\t" +(m.isProcessExec() ? "√" : "x")
					+" 激励ID : " +m.processID
					+ " 激励名称 : " + m.processName
					+ "( 激励参数 : " + ((m.processParam == "NULL")?"空":m.getProcessParam())
					+ " 激励状态 :" + ((m.processStatus == "NULL")?"空":m.getProcessStatus())
					+")";
		}
		tmp = tmp + " ]\n  -->测试执行状态: [ " + state + " ]\n  -->结果状态: [ "+ result.getResultDetail()+" ]";
		return tmp;
	}
}