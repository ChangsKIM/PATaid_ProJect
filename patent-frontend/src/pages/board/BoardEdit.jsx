// src/pages/board/BoardEdit.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/AuthContext';

/**
 * 게시글 수정 페이지
 * URL 예: /board/:category/:id/edit
 * props.category로 넘어올 수도 있고, useParams로 받을 수도 있음
 */
function BoardEdit({ category: propCategory }) {
  const navigate = useNavigate();
  const { category: routeCategory, id } = useParams();
  const { isAuthenticated, user } = useAuth();

  const category = propCategory || routeCategory;

  const [post, setPost] = useState({
    title: '',
    content: '',
    tag: '',
  });

  useEffect(() => {
    // 로그인 체크
    if (!isAuthenticated) {
      alert('로그인 후 수정 가능합니다.');
      navigate('/login');
      return;
    }
    // 게시글 가져오기
    fetch(`/api/board/${category}/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error('Failed to fetch post');
        return res.json();
      })
      .then((data) => {
        // 수정 시 말머리도 편집 가능하도록
        setPost({
          title: data.title || '',
          content: data.content || '',
          tag: data.tag || '',
        });
        // 작성자 본인 or 관리자만 수정
        if (user?.role !== 'ROLE_ADMIN' && user?.userId !== data.writerId) {
          alert('수정 권한이 없습니다.');
          navigate(`/board/${category}/${id}`);
        }
      })
      .catch((err) => console.error(err));
  }, [category, id, isAuthenticated, user, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPost((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // PUT /api/board/{category}/{id}
    fetch(`/api/board/${category}/${id}`, {
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
        navigate(`/board/${category}/${data.id}`);
      })
      .catch((err) => console.error(err));
  };

  return (
    <div style={{ padding: '10px' }}>
      <h2>[{category}] 글 수정</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>말머리(tag): </label>
          <input
            name="tag"
            value={post.tag}
            onChange={handleChange}
          />
        </div>
        <div>
          <label>제목: </label>
          <input
            name="title"
            value={post.title}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>내용: </label>
          <textarea
            name="content"
            value={post.content}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit">수정</button>
        <button onClick={() => navigate(`/board/${category}/${id}`)}>취소</button>
      </form>
    </div>
  );
}

export default BoardEdit;
