// src/pages/notice/NoticeBoardEdit.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/AuthContext';
import './NoticeBoardEdit.css';

function NoticeBoardEdit() {
  const navigate = useNavigate();
  const { id } = useParams();
  const { isAuthenticated, user } = useAuth();

  const [post, setPost] = useState({
    title: '',
    content: '',
    tag: '[공지사항]',
  });

  useEffect(() => {
    if (!isAuthenticated) {
      alert('로그인 후 수정 가능합니다.');
      navigate('/login');
      return;
    }
    fetch(`/api/board/NOTICE/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch post');
        return res.json();
      })
      .then((data) => {
        setPost({
          title: data.title || '',
          content: data.content || '',
          tag: data.tag || '[공지사항]',
        });
        // 관리자 or 작성자만 수정 (실제론 공지=관리자만)
        if (user?.role !== 'ROLE_ADMIN' && user?.userId !== data.writerId) {
          alert('수정 권한이 없습니다.');
          navigate(`/notice/${id}`);
        }
      })
      .catch((err) => console.error('공지사항 수정 로딩 오류:', err));
  }, [id, isAuthenticated, user, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPost((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    fetch(`/api/board/NOTICE/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(post),
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to update post');
        return res.json();
      })
      .then((data) => {
        alert('수정 완료');
        navigate(`/notice/${data.id}`);
      })
      .catch((err) => console.error('공지사항 수정 오류:', err));
  };

  return (
    <div className="notice-board-edit">
      <h2>공지사항 수정</h2>
      <form onSubmit={handleSubmit}>
        <div className="notice-field">
          <label>말머리:</label>
          <input
            name="tag"
            value={post.tag}
            onChange={handleChange}
            readOnly
          />
        </div>
        <div className="notice-field">
          <label>제목:</label>
          <input
            name="title"
            value={post.title}
            onChange={handleChange}
            required
          />
        </div>
        <div className="notice-field">
          <label>내용:</label>
          <textarea
            name="content"
            value={post.content}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit">수정</button>
        <button type="button" onClick={() => navigate(`/notice/${id}`)}>
          취소
        </button>
      </form>
    </div>
  );
}

export default NoticeBoardEdit;
