// src/services/api.js
import axios from 'axios';

// ------------------------------
// 1) Axios 인스턴스 생성
// ------------------------------
const api = axios.create({
  baseURL: 'http://localhost:8080', // 스프링부트 서버 주소
  withCredentials: false,          // 쿠키 인증이 필요하면 true
});

// ------------------------------
// 2) 요청 인터셉터: localStorage에서 토큰을 읽어
//    매 요청마다 Authorization 헤더에 추가
// ------------------------------
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// ------------------------------
// 3) 로그인 API
//    백엔드 /api/login (POST)
//    => { success, message, token, nickname, role }
// ------------------------------
export async function login(username, password) {
  const response = await api.post('/api/login', { username, password });
  if (!response.data.success) {
    throw new Error(response.data.message || '로그인 실패');
  }
  return response.data; // { success, message, token, nickname, role }
}

// ------------------------------
// 4) 개인 회원가입
//    백엔드 /api/register/individual (POST)
// ------------------------------
export async function registerIndividual(userData) {
  const response = await api.post('/api/register/individual', userData);
  return response.data;
}

// ------------------------------
// 5) 기업 회원가입
//    백엔드 /api/register/corporate (POST)
// ------------------------------
export async function registerCorporate(userData) {
  const response = await api.post('/api/register/corporate', userData);
  return response.data;
}

// ------------------------------
// 6) 아이디 중복확인
//    GET /api/register/check-username?username=...
// ------------------------------
export async function checkUsername(username) {
  const response = await api.get(`/api/register/check-username?username=${username}`);
  return response.data;
}

// ------------------------------
// 7) 닉네임 중복확인
//    GET /api/register/check-nickname?nickname=...
// ------------------------------
export async function checkNickname(nickname) {
  const response = await api.get(`/api/register/check-nickname?nickname=${nickname}`);
  return response.data;
}

// ------------------------------
// 8) 토큰 유효성 검증
//    GET /api/validate-token
//    => { valid: true/false, nickname: '...' }
// ------------------------------
export async function validateTokenAPI() {
  const response = await api.get('/api/validate-token');
  return response.data;
}

// ------------------------------
// 9) api 인스턴스 export default
// ------------------------------
export default api;
