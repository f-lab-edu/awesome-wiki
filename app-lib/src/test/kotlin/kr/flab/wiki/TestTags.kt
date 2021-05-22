/**
 * 빌드 스크립트 buildScripts/testing.gradle 에 정의한 테스트 그룹 상수 모음.
 * 개별 테스트들에 이 파일에 정의한 Tag 들을 반드시 선언해야 테스트를 단계별로 실행할 수 있습니다.
 */
package kr.flab.wiki

/**
 * Unit test 또는 "Small test"
 */
const val TAG_TEST_UNIT = "unit"

/**
 * Integration test 또는 "Medium test"
 */
const val TAG_TEST_INTEGRATION = "integration"

/**
 * End to End test 또는 "Large test"
 */
const val TAG_TEST_E2E = "e2e"
