package com.moderncalendar.feature.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.moderncalendar.core.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow

/**
 * Paginated event list with infinite scrolling
 */
@Composable
fun PaginatedEventList(
    events: Flow<PagingData<EventEntity>>,
    onEventClick: (String) -> Unit = {},
    onLoadMore: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val lazyPagingItems = events.collectAsLazyPagingItems()
    val listState = rememberLazyListState()
    
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id }
        ) { index ->
            val event = lazyPagingItems[index]
            if (event != null) {
                EventItem(
                    event = event,
                    onClick = { onEventClick(event.id) }
                )
            } else {
                EventItemPlaceholder()
            }
            
            // Load more when approaching the end
            if (index == lazyPagingItems.itemCount - 1) {
                onLoadMore()
            }
        }
        
        // Loading indicator at the bottom
        if (lazyPagingItems.loadState.append is androidx.paging.LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        
        // Error state
        if (lazyPagingItems.loadState.append is androidx.paging.LoadState.Error) {
            item {
                ErrorItem(
                    onRetry = { lazyPagingItems.retry() }
                )
            }
        }
    }
}

@Composable
private fun EventItemPlaceholder(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder for event color indicator
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Placeholder for title
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                        )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Placeholder for time
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(12.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun ErrorItem(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Failed to load events",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Retry")
            }
        }
    }
}
