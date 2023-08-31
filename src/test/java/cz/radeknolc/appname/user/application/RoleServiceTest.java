package cz.radeknolc.appname.user.application;

import cz.radeknolc.appname.shared.problem.domain.exception.Problem;
import cz.radeknolc.appname.user.domain.entity.Role;
import cz.radeknolc.appname.user.domain.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    private RoleService underTest;

    @BeforeEach
    void setUp() {
        underTest = new RoleService(roleRepository);
    }

    @Test
    void getDefaultRole_ExistingDefaultRole_Role() {
        // given
        Role expectedRole = new Role("EXAMPLE_ROLE");
        given(roleRepository.findRoleByName(anyString())).willReturn(Optional.of(expectedRole));

        // when
        Role role = underTest.getDefaultRole();

        //then
        assertThat(role).isEqualTo(expectedRole);
    }

    @Test
    void getDefaultRole_NotExistingDefaultRole_Problem() {
        // given
        given(roleRepository.findRoleByName(anyString())).willReturn(Optional.empty());

        // when
        //then
        assertThatThrownBy(() -> underTest.getDefaultRole()).isInstanceOf(Problem.class).hasMessage("DEFAULT_ROLE_NOT_EXISTS");
    }
}