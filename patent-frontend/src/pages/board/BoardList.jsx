// src/pages/board/BoardList.jsx
import React, { useEffect, useState } from 'react';
import { useAuth } from '../../hooks/AuthContext';
import { useNavigate, Link } from 'react-router-dom';
import api from '../../services/api';
import '../../css/BoardList.css'; // CSS 파일

function BoardList({ category }) {
  const { isAuthenticated, user, isAuthLoaded } = useAuth();
  const navigate = useNavigate();

  const [posts, setPosts] = useState([]);
  const [pinnedPosts, setPinnedPosts] = useState([]);
  const [page, setPage] = useState(0);
  const size = 10;
  const [totalPages, setTotalPages] = useState(1);

  // (A) 게시판 제목 맵핑
  const boardTitleMap = {
    NOTICE: '공지사항 게시판 목록',
    QNA: '질문/건의사항 게시판 목록',
    FREE: '자유 게시판 목록',
    JOB: '구인/구직 게시판 목록',
  };
  const boardTitle = boardTitleMap[category] || `${category} 게시판 목록`;

  // (B) 하나의 useEffect로 통합: 로그인 체크 + 게시글 로드
  useEffect(() => {
    // 아직 AuthContext에서 로딩이 안 끝났으면, 아무것도 하지 않음
    if (!isAuthLoaded) return;

    // 1) 접근 권한 체크
    if ((category === 'NOTICE' || category === 'QNA') && !isAuthenticated) {
      alert('로그인 후 열람 가능합니다.');
      navigate('/login');
      return;
    }
    if (category === 'JOB' && !isAuthenticated) {
      alert('비공개 게시판입니다. 로그인 후 이용해주세요.');
      navigate('/login');
      return;
    }
    // FREE는 비로그인도 열람 가능하므로 조건 없음

    // 2) 게시글 목록 로드
    async function loadPosts() {
      try {
        const res = await api.get(`/api/board/${category}?page=${page}&size=${size}`);
        const data = res.data;
        if (category === 'NOTICE') {
          // 공지사항: pinnedPosts로 처리
          setPinnedPosts(data.content || []);
          setPosts([]);
        } else {
          setPosts(data.content || []);
          setPinnedPosts([]);
        }
        setTotalPages(data.totalPages || 1);
      } catch (err) {
        console.error('게시판 목록 로딩 실패:', err);
        alert('게시판 목록 로딩 실패: ' + err.message);
      }
    }

    loadPosts();
  }, [category, isAuthenticated, isAuthLoaded, navigate, page, size]);

  // (C) 글쓰기 버튼
  const handleWrite = () => {
    if (category === 'NOTICE' && user?.role !== 'ROLE_ADMIN') {
      alert('관리자만 작성할 수 있습니다.');
      return;
    } else if (
      (category === 'FREE' || category === 'QNA' || category === 'JOB') &&
      !isAuthenticated
    ) {
      alert('로그인 후 작성 가능합니다.');
      return;
    }
    navigate(`/board/${category.toLowerCase()}/write`);
  };

  // (D) 아직 Auth 로딩이 안 끝났다면 표시
  if (!isAuthLoaded) {
    return <div style={{ textAlign: 'center', marginTop: '20px' }}>로딩 중...</div>;
  }

  return (
    <div className="board-list-container">
      <h2>{boardTitle}</h2>

      {/* pinnedPosts (공지) */}
      {pinnedPosts.length > 0 && (
        <table className="board-list-table pinned-table">
          <thead>
            <tr>
              <th>순번</th>
              <th>제목</th>
              <th>조회수</th>
              <th>작성일</th>
            </tr>
          </thead>
          <tbody>
            {pinnedPosts.map((post, index) => {
              const dateObj = new Date(post.createdAt);
              const yyyy = dateObj.getFullYear();
              const mm = String(dateObj.getMonth() + 1).padStart(2, '0');
              const dd = String(dateObj.getDate()).padStart(2, '0');
              const formattedDate = `${yyyy}-${mm}-${dd}`;

              return (
                <tr key={post.id}>
                  <td>{index + 1}</td>
                  <td>
                    <Link to={`/board/${category.toLowerCase()}/${post.id}`}>
                      {post.title}
                    </Link>
                  </td>
                  <td>{post.views}</td>
                  <td>{formattedDate}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      )}

      {/* 일반글 */}
      {posts.length > 0 && (
        <table className="board-list-table">
          <thead>
            <tr>
              <th>순번</th>
              <th>제목</th>
              <th>조회수</th>
              <th>작성일</th>
            </tr>
          </thead>
          <tbody>
            {posts.map((post, index) => {
              const seq = page * size + (index + 1);

              const dateObj = new Date(post.createdAt);
              const yyyy = dateObj.getFullYear();
              const mm = String(dateObj.getMonth() + 1).padStart(2, '0');
              const dd = String(dateObj.getDate()).padStart(2, '0');
              const formattedDate = `${yyyy}-${mm}-${dd}`;

              return (
                <tr key={post.id}>
                  <td>{seq}</td>
                  <td>
                    <Link to={`/board/${category.toLowerCase()}/${post.id}`}>
                      {post.title}
                    </Link>
                  </td>
                  <td>{post.views}</td>
                  <td>{formattedDate}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      )}

      {/* 페이징 영역 */}
      <div className="board-list-pagination">
        <button
          onClick={() => setPage((prev) => Math.max(prev - 1, 0))}
          disabled={page === 0}
        >
          이전
        </button>
        <span> {page + 1} / {totalPages} </span>
        <button
          onClick={() => setPage((prev) => (prev + 1 < totalPages ? prev + 1 : prev))}
          disabled={page + 1 >= totalPages}
        >
          다음
        </button>
      </div>

      {/* 글쓰기 버튼: 오른쪽 하단 정렬 */}
      <div className="board-list-footer">
        <button className="write-btn" onClick={handleWrite}>
          글쓰기
        </button>
      </div>
    </div>
  );
}

export default BoardList;
