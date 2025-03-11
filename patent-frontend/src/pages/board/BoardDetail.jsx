// src/pages/board/BoardDetail.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/AuthContext';
import api from '../../services/api';

function BoardDetail({ category: propCategory }) {
  const navigate = useNavigate();
  const { category: routeCategory, id } = useParams();
  const { isAuthenticated, user } = useAuth();

  // 우선순위: propCategory -> routeCategory
  const category = propCategory || routeCategory;

  // 카테고리별 제목 매핑
  const boardTitleMap = {
    NOTICE: '공지사항 상세보기',
    QNA: '질문/건의 상세보기',
    FREE: '자유 게시판 상세보기',
    JOB: '구인/구직 상세보기',
  };
  const pageTitle = boardTitleMap[category] || `[${category}] 게시글 상세보기`;

  const [post, setPost] = useState(null);

  // 상세 조회
  useEffect(() => {
    async function loadPost() {
      try {
        // GET /api/board/{category}/{id}
        const res = await api.get(`/api/board/${category}/${id}`);
        setPost(res.data);
      } catch (err) {
        console.error('상세보기 오류:', err);
        alert('게시글을 불러오지 못했습니다: ' + err.message);
      }
    }
    loadPost();
  }, [category, id]);

  // 삭제
  const handleDelete = async () => {
    if (!isAuthenticated) {
      alert('로그인 후 삭제 가능합니다.');
      return;
    }
    try {
      const res = await api.delete(`/api/board/${category}/${id}`);
      if (res.status === 200) {
        alert('삭제되었습니다.');
        navigate(`/board/${category}`);
      }
    } catch (err) {
      console.error('삭제 오류:', err);
      alert('게시글 삭제 실패: ' + err.message);
    }
  };

  // 로딩 중
  if (!post) {
    return <div>로딩 중...</div>;
  }

  // 작성자 or 관리자
  const isWriter = (user?.userId === post.writerId);
  const isAdmin = (user?.role === 'ROLE_ADMIN');

  // 수정: 작성자만
  const canEdit = isAuthenticated && isWriter;
  // 삭제: 작성자 or 관리자
  const canDelete = isAuthenticated && (isWriter || isAdmin);

  // 작성일 YYYY-MM-DD 변환
  function formatDate(dateStr) {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }
  const createdDate = formatDate(post.createdAt);

  return (
    <div style={{ padding: '10px' }}>
      <h2>{pageTitle}</h2>

      <div>
        <strong>말머리(tag): </strong> {post.tag}
      </div>
      <div>
        <strong>제목: </strong> {post.title}
      </div>
      <div>
        <strong>작성자: </strong> {post.writerId}
      </div>
      <div>
        <strong>조회수: </strong> {post.views}
      </div>
      <div>
        <strong>작성일: </strong> {createdDate}
      </div>
      <hr />
      <p>{post.content}</p>
      <hr />

      {/* 첨부파일이 있는 경우 */}
      {post.storedFilename && (
        <div>
          <strong>첨부파일: </strong> {post.storedFilename}
        </div>
      )}

      <div style={{ marginTop: '10px' }}>
        <button onClick={() => navigate(`/board/${category}`)}>목록</button>
        {canEdit && (
          <button onClick={() => navigate(`/board/${category}/${id}/edit`)}>
            수정
          </button>
        )}
        {canDelete && (
          <button onClick={handleDelete}>
            삭제
          </button>
        )}
      </div>
    </div>
  );
}

export default BoardDetail;
