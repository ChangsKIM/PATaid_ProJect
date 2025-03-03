package kr.pataidcompany.patent_backend.service;

import kr.pataidcompany.patent_backend.model.Board;
import kr.pataidcompany.patent_backend.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepo;

    // 작성
    public Board createPost(Board post) {
        return boardRepo.save(post);
    }

    // 조회수 증가 포함 조회
    public Board getPostAndIncreaseViews(Long id) {
        Board post = boardRepo.findById(id).orElse(null);
        if (post != null) {
            post.setViews(post.getViews() + 1);
            boardRepo.save(post);
        }
        return post;
    }

    // **추가**: 단순 조회 (조회수 증가 없음)
    public Board getPost(Long id) {
        return boardRepo.findById(id).orElse(null);
    }

    // 수정
    public Board updatePost(Long id, Board updated) {
        Board post = boardRepo.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        post.setTitle(updated.getTitle());
        post.setContent(updated.getContent());
        post.setTag(updated.getTag());
        // file attachment, category 변경 여부는 필요 시
        return boardRepo.save(post);
    }

    // 삭제
    public boolean deletePost(Long id) {
        if (boardRepo.existsById(id)) {
            boardRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // 카테고리별 목록 (페이징)
    public Page<Board> listByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return boardRepo.findByCategoryOrderByCreatedAtDesc(category, pageable);
    }

    // 검색 (제목 or 내용에 keyword 포함, 페이징)
    public Page<Board> searchPosts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return boardRepo.findByTitleContainingOrContentContainingIgnoreCase(keyword, keyword, pageable);
    }
}
