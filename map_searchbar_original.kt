      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(52.dp)
            .shadow(elevation = 4.dp, shape = CircleShape)
            .background(Color.White, CircleShape)
            .border(
              width = 1.5.dp,
              color = Color(0xFFCBD5E1),
              shape = CircleShape
            )
            .clickable { onBack() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "Kembali ke Home",
            tint = Color(0xFF1E232A),
            modifier = Modifier.size(20.dp)
          )
        }
        Spacer(modifier = Modifier.width(12.dp))
        OutlinedTextField(
          value = localSearchQuery,
          onValueChange = { localSearchQuery = it },
          placeholder = { Text("Mulai jelajahi disini", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4A5568)) },
          textStyle = TextStyle(color = Color(0xFF1E232A), fontSize = 14.sp),
          trailingIcon = {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(end = 8.dp)
            ) {
              if (localSearchQuery.isNotEmpty()) {
                IconButton(
                  onClick = {
                    localSearchQuery = ""
                    onSearchChange("")
                    focusManager.clearFocus()
                  },
                  modifier = Modifier.size(24.dp)
                ) {
                  Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Hapus",
                    tint = Color(0xFF4A5568),
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(4.dp))
              }
              Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Cari",
                tint = DiajakOrange,
                modifier = Modifier
                  .size(22.dp)
                  .clickable {
                    onSearchChange(localSearchQuery)
                    focusManager.clearFocus()
                  }
              )
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(24.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color(0xFF1E232A),
            unfocusedTextColor = Color(0xFF1E232A),
            cursorColor = DiajakOrange,
            focusedPlaceholderColor = Color(0xFF4A5568),
            unfocusedPlaceholderColor = Color(0xFF4A5568),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
          ),
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
          keyboardActions = KeyboardActions(onSearch = {
            onSearchChange(localSearchQuery)
            focusManager.clearFocus()
          }),
          modifier = Modifier
            .weight(1f)
            .height(52.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp))
            .border(
              width = if (isSearchFocused) 2.dp else 1.5.dp,
              color = if (isSearchFocused) DiajakOrange else Color(0xFFCBD5E1),
              shape = RoundedCornerShape(24.dp)
            )
            .onFocusChanged { state ->
              isSearchFocused = state.isFocused
            }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
          modifier = Modifier
            .size(52.dp)
            .shadow(elevation = 4.dp, shape = CircleShape)
            .background(if (priceFilter != "all" || ratingFilter != "all") DiajakOrange else Color.White, CircleShape)
            .border(
              width = 1.5.dp,
              color = if (priceFilter != "all" || ratingFilter != "all") DiajakOrange else Color(0xFFCBD5E1),
              shape = CircleShape
            )
            .clickable {
              focusManager.clearFocus()
              showFilterDialog = true
            },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Outlined.Tune,
            contentDescription = "Filter",
            tint = if (priceFilter != "all" || ratingFilter != "all") Color.White else Color(0xFF1E232A)
          )
        }
      }
