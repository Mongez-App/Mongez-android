package com.iti.mongez.presentation.organization.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.R
import com.iti.mongez.designsystem.components.card.TeamCard
import com.iti.mongez.designsystem.components.common.AppEmptyState
import com.iti.mongez.designsystem.components.loading.AppCircularLoading
import com.iti.mongez.designsystem.components.tabs.AppSegmentedTabs
import com.iti.mongez.designsystem.components.tabs.SegmentedTabItem
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.organization.uiState.OrganizationUiState
import com.iti.mongez.presentation.organization.viewmodel.OrganizationViewModel
import androidx.compose.ui.text.font.FontWeight
import com.iti.mongez.designsystem.components.search.AppSearchBar
import com.iti.mongez.presentation.organization.components.DiscoverTeamCard

@Composable
fun OrganizationScreen(
    innerPadding: PaddingValues,
    viewModel: OrganizationViewModel = hiltViewModel(),
    onNavigateToTeamCourses: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = Theme.spacing.md)
    ) {
        Spacer(modifier = Modifier.height(Theme.spacing.md))
        
        Text(
            text = stringResource(com.iti.mongez.presentation.R.string.nav_organization),
            style = Theme.typography.headline.medium,
            color = Theme.colorScheme.text.primary,
            modifier = Modifier.padding(bottom = Theme.spacing.lg)
        )

        val tabs = listOf(
            SegmentedTabItem(
                title = "My Teams"
            ),
            SegmentedTabItem(
                title = "Discover"
            )
        )

        AppSegmentedTabs(
            items = tabs,
            selectedIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        when (val uiState = state) {
            is OrganizationUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AppCircularLoading()
                }
            }
            is OrganizationUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.message, color = Theme.colorScheme.state.error)
                }
            }
            is OrganizationUiState.Success -> {
                if (selectedTabIndex == 0) {
                    // My Teams
                    if (uiState.myTeams.isEmpty()) {
                        AppEmptyState(
                            title = "No Teams Yet",
                            description = "You haven't joined any teams yet. Search for a code or browse public campus groups.",
                            illustration = {
                                androidx.compose.foundation.Image(
                                    painter = painterResource(id = R.drawable.no_teams),
                                    contentDescription = null,
                                    modifier = Modifier.size(120.dp)
                                )
                            },
                            actionText = "Join Your First Team",
                            onAction = { selectedTabIndex = 1 } // Switch to discover
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md),
                            contentPadding = PaddingValues(bottom = Theme.spacing.xxl)
                        ) {
                            items(uiState.myTeams) { team ->
                                TeamCard(
                                    teamName = team.name,
                                    photoUrl = team.photoUrl,
                                    progress = team.progress,
                                    eventsCount = team.events.size,
                                    onClick = { 
                                        onNavigateToTeamCourses(team.id)
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // Discover
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = Theme.spacing.sm, vertical = Theme.spacing.sm)
                    ) {
                        var searchQuery by remember { mutableStateOf("") }
                        AppSearchBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            placeholder = "Enter Team name here",
                            modifier = Modifier.padding(bottom = Theme.spacing.lg)
                        )
                        
                        val filteredPending = uiState.discoverTeams.pendingInvitations.filter {
                            it.name.contains(searchQuery, ignoreCase = true) || 
                            it.organizationName.contains(searchQuery, ignoreCase = true)
                        }
                        
                        val filteredTrending = uiState.discoverTeams.trendingTeams.filter {
                            it.name.contains(searchQuery, ignoreCase = true) || 
                            (it.organizationName ?: "").contains(searchQuery, ignoreCase = true)
                        }
                        
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md),
                            contentPadding = PaddingValues(bottom = Theme.spacing.xxl)
                        ) {
                            if (filteredPending.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Pending Invitations",
                                        style = Theme.typography.headline.small,
                                        fontWeight = FontWeight.Bold,
                                        color = Theme.colorScheme.text.primary,
                                        modifier = Modifier.padding(bottom = Theme.spacing.sm)
                                    )
                                }
                                items(items = filteredPending, key = { "pending_${it.id}" }) { invitation ->
                                    DiscoverTeamCard(
                                        teamName = invitation.name,
                                        organizationName = invitation.organizationName,
                                        imageUrl = invitation.imageUrl,
                                        appliedDate = invitation.appliedDate,
                                        onClick = { },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                                item {
                                    Spacer(modifier = Modifier.height(Theme.spacing.md))
                                }
                            }

                            if (filteredTrending.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Trending Teams",
                                        style = Theme.typography.headline.small,
                                        fontWeight = FontWeight.Bold,
                                        color = Theme.colorScheme.text.primary,
                                        modifier = Modifier.padding(bottom = Theme.spacing.sm)
                                    )
                                }
                                items(items = filteredTrending, key = { "trending_${it.teamId}" }) { team ->
                                    DiscoverTeamCard(
                                        teamName = team.name,
                                        organizationName = team.organizationName ?: "Unknown Organization",
                                        imageUrl = team.imageUrl,
                                        onClick = { },
                                        modifier = Modifier.animateItem()
                                    )
                                }
                            }
                            
                            if (filteredPending.isEmpty() && filteredTrending.isEmpty()) {
                                item {
                                    AppEmptyState(
                                        title = "No Teams Found",
                                        description = "Unfortunately no teams found.",
                                        illustration = {
                                            androidx.compose.foundation.Image(
                                                painter = painterResource(id = R.drawable.no_teams),
                                                contentDescription = null,
                                                modifier = Modifier.size(120.dp)
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
