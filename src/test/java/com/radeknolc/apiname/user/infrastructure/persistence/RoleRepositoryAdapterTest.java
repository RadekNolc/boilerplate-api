package com.radeknolc.apiname.user.infrastructure.persistence;

import com.radeknolc.apiname.user.infrastructure.persistence.repository.RoleEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryAdapterTest {

    @Mock
    private RoleEntityRepository roleEntityRepository;

    private RoleRepositoryAdapter underTest;

    @BeforeEach
    void setUp() {
        underTest = new RoleRepositoryAdapter(roleEntityRepository);
    }

    @Test
    void findRoleByName_ArgumentCapturing_Captured() {
        // given
        String name = "EXAMPLE_ROLE";

        // when
        underTest.findRoleByName(name);

        // then
        ArgumentCaptor<String> nameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(roleEntityRepository).findByName(nameArgumentCaptor.capture());

        String capturedName = nameArgumentCaptor.getValue();

        assertThat(capturedName).isEqualTo(name);
    }
}