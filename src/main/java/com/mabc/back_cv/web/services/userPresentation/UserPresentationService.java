package com.mabc.back_cv.web.services.userPresentation;

import com.mabc.back_cv.web.dto.UserPresentationDTO;
import org.springframework.data.domain.Page;

public interface UserPresentationService{

    public Page<UserPresentationDTO> getAll(String searchText, Long userId, Integer page, Integer size);

    public UserPresentationDTO findById(Long id);

    public UserPresentationDTO save(UserPresentationDTO userPresentation);

    public void delete(Long id);
}