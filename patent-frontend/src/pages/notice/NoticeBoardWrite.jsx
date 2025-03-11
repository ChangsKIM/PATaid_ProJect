// src/pages/notice/NoticeBoardWrite.jsx
import React, { useEffect, useState } from 'react';
import { useAuth } from '../../hooks/AuthContext';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';
import './NoticeBoardWrite.css';

function NoticeBoardWrite() {
  const { isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  // 고정 말머리
  const tag = '[공지사항]';

  useEffect(() => {
    if (!isAuthenticated) {
      alert('로그인 후 작성 가능합니다.');
      navigate('/login');
      return;
    }
    // 관리자만
    if (user?.role !== 'ROLE_ADMIN') {
      alert('관리자만 작성할 수 있습니다.');
      navigate('/notice');
    }
  }, [isAuthenticated, user, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const postData = { title, content, tag };
      const res = await api.post('/api/board/NOTICE', postData);
      const data = res.data;
      if (!data.id) {
        alert('서버 응답에 게시글 ID가 없습니다.');
        return;
      }
      alert('작성 완료');
      navigate(`/notice/${data.id}`);
    } catch (err) {
      console.error('공지사항 작성 실패:', err);
      alert('작성 실패: ' + err.message);
    }
  };

  return (
    <div className="notice-board-write">
      <h2>공지사항 작성</h2>
      <form onSubmit={handleSubmit}>
        <div className="notice-field">
          <label>말머리: </label>
          <input value={tag} readOnly />
        </div>
        <div className="notice-field">
          <label>제목: </label>
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>
        <div className="notice-field">
          <label>내용: </label>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
          />
        </div>

        <button type="submit">등록</button>
        <button type="button" onClick={() => navigate('/notice')}>
          취소
        </button>
      </form>
    </div>
  );
}

export default NoticeBoardWrite;