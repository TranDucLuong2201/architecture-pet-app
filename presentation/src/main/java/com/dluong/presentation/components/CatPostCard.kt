package com.dluong.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dluong.domain.model.PetPost

@Composable
fun CatPostCard(
    post: PetPost,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onShareClick: (String) -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            // Header - User Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(post.user?.avatarUrl ?: "https://via.placeholder.com/40")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { onUserClick(post.userId) },
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.user?.displayName ?: "Unknown User",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "@${post.user?.username ?: "unknown"}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                if (!post.isFollowed) {
                    Button(
                        onClick = { /* Handle follow */ },
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text("Follow", fontSize = 12.sp)
                    }
                }
            }

            // Cat Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Cat Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    IconButton(
                        onClick = { onLikeClick(post.id) }
                    ) {
                        Icon(
                            imageVector = if (post.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (post.isLiked) Color.Red else Color.Gray
                        )
                    }

                    IconButton(
                        onClick = { onCommentClick(post.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBox,
                            contentDescription = "Comment",
                            tint = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = { onShareClick(post.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Gray
                        )
                    }
                }
            }

            // Like Count
            if (post.likeCount > 0) {
                Text(
                    text = "${post.likeCount} likes",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 14.sp
                )
            }

            // Caption
            if (post.caption.isNotBlank()) {
                Text(
                    text = buildString {
                        append(post.user?.username ?: "")
                        append(" ")
                        append(post.caption)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Comment Count
            if (post.commentCount > 0) {
                Text(
                    text = "View all ${post.commentCount} comments",
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { onCommentClick(post.id) },
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

