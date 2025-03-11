// src/pages/notice/NoticeBoardDetail.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/AuthContext';
import './NoticeBoardDetail.css';

function NoticeBoardDetail() {
  const navigate = useNavigate();
  const { id } = useParams(); // /notice/:id
  const { isAuthenticated, user } = useAuth();

  const [post, setPost] = useState(null);

  // 상세 조회
  useEffect(() => {
    if (!isAuthenticated) {
      alert('로그인 후 열람 가능합니다.');
      navigate('/login');
      return;
    }

    fetch(`/api/board/NOTICE/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch post');
        return res.json();
      })
      .then((data) => setPost(data))
      .catch((err) => console.error('공지사항 상세보기 오류:', err));
  }, [id, isAuthenticated, navigate]);

  const handleDelete = () => {
    if (!isAuthenticated) {
      alert('로그인 후 삭제 가능합니다.');
      return;
    }
    // 관리자 or 작성자
    fetch(`/api/board/NOTICE/${id}`, {
      method: 'DELETE',
    })
      .then((res) => {
        if (!res.ok) throw new Error('Failed to delete');
        return res.text();
      })
      .then(() => {
        alert('삭제되었습니다.');
        navigate('/notice');
      })
      .catch((err) => console.error('삭제 오류:', err));
  };

  if (!post) {
    return <div className="notice-detail-loading">로딩 중...</div>;
  }

  const canEdit =
    user?.role === 'ROLE_ADMIN' || user?.userId === post.writerId;

  return (
    <div className="notice-board-detail">
      <h2>공지사항 상세보기</h2>

      <div className="notice-field">
        <strong>말머리(tag):</strong> {post.tag}
      </div>
      <div className="notice-field">
        <strong>제목:</strong> {post.title}
      </div>
      <div className="notice-field">
        <strong>작성자:</strong> {post.writerId}
      </div>
      <div className="notice-field">
        <strong>조회수:</strong> {post.views}
      </div>
      <div className="notice-field">
        <strong>작성일:</strong> {post.createdAt}
      </div>
      <hr />
      <div className="notice-content">{post.content}</div>
      <hr />

      {/* 파일첨부 예시 */}
      {post.storedFilename && (
        <div className="notice-field">
          <strong>첨부파일:</strong> {post.storedFilename}
        </div>
      )}

      <div className="notice-buttons">
        <button onClick={() => navigate('/notice')}>목록</button>
        {canEdit && (
          <>
            <button onClick={() => navigate(`/notice/${id}/edit`)}>수정</button>
            <button onClick={handleDelete}>삭제</button>
          </>
        )}
      </div>
    </div>
  );
}

export default NoticeBoardDetail;
