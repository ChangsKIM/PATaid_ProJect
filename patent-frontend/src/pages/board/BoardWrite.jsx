// src/pages/board/BoardWrite.jsx
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../hooks/AuthContext';
import { useNavigate } from 'react-router-dom';
import api from '../../services/api';

/**
 * props.category: "NOTICE" | "QNA" | "FREE" | "JOB"
 */
function BoardWrite({ category }) {
  const { isAuthenticated, user } = useAuth();
  const navigate = useNavigate();

  const [tag, setTag] = useState('');     // 말머리
  const [title, setTitle] = useState(''); // 제목
  const [content, setContent] = useState(''); // 본문
  const [tagOptions, setTagOptions] = useState([]); // 말머리 선택 옵션

  // 시나리오상 말머리 옵션 설정
  useEffect(() => {
    if (category === 'NOTICE') {
      setTagOptions(['[공지사항]']);
      setTag('[공지사항]');
    } else if (category === 'QNA') {
      setTagOptions(['[건의사항]', '[질문]', '[하고 싶은 말]']);
    } else if (category === 'FREE') {
      setTagOptions(['[잡담]', '[유머]', '[하고 싶은 말]']);
    } else if (category === 'JOB') {
      setTagOptions(['[구인]', '[구직]', '[의뢰]']);
    }
  }, [category]);

  // 권한 체크: 로그인/관리자 여부
  useEffect(() => {
    if (!isAuthenticated) {
      alert('로그인 후 작성 가능합니다.');
      navigate('/login');
    }
    // NOTICE는 관리자만
    if (category === 'NOTICE' && user?.role !== 'ROLE_ADMIN') {
      alert('관리자만 작성할 수 있습니다.');
      navigate('/board/notice');
    }
  }, [category, isAuthenticated, user, navigate]);

  // 폼 제출(글 작성)
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // POST /api/board/{category}
      const postData = { title, content, tag };
      const res = await api.post(`/api/board/${category}`, postData);
      const data = res.data;

      // data: { id: 123, title: "...", ... } 형태라고 가정
      if (!data.id) {
        // 혹시 id가 없으면 예외 처리
        alert('서버 응답에 게시글 ID가 없습니다.');
        return;
      }

      alert('작성 완료');
      // 상세 페이지로 이동: /board/{category}/글ID
      navigate(`/board/${category.toLowerCase()}/${data.id}`);
    } catch (err) {
      console.error('글 작성 실패:', err);
      alert('글 작성 실패: ' + err.message);
    }
  };

  return (
    <div>
      <h2>{category} 글 작성</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>말머리: </label>
          {/* NOTICE 카테고리는 고정, 그 외는 select */}
          {category === 'NOTICE' ? (
            <input value={tag} readOnly />
          ) : (
            <select value={tag} onChange={(e) => setTag(e.target.value)}>
              <option value="">--말머리 선택--</option>
              {tagOptions.map((opt) => (
                <option key={opt} value={opt}>
                  {opt}
                </option>
              ))}
            </select>
          )}
        </div>

        <div>
          <label>제목: </label>
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
        </div>

        <div>
          <label>내용: </label>
          <textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            required
          />
        </div>

        <button type="submit">등록</button>
        <button type="button" onClick={() => navigate(`/board/${category.toLowerCase()}`)}>
          취소
        </button>
      </form>
    </div>
  );
}

export default BoardWrite;
